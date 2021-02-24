package org.hqu.vibsignal_analysis.util.algorithm;

import org.hqu.vibsignal_analysis.mapper.entity.ExpResult;
import org.hqu.vibsignal_analysis.mapper.entity.Experiment;
import org.hqu.vibsignal_analysis.service.congfiguration.ExpConfig;
import org.hqu.vibsignal_analysis.util.DateConverter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

public class DensityClusteringPos {
    static int size1;
    static int size2;
    static int windowSize=24;
    static int label=1;
    static WebSocketSession session;
    public ExpResult dcp(WebSocketSession webSocketSession, HttpSession httpSession, double correlation, int windowSize, int clusterSize, String filePath1, String filePath2, String dataId) throws Exception {
        this.session = webSocketSession;
        this.windowSize =windowSize;
        double N = correlation;
        DensityClusteringPos hc = new DensityClusteringPos();
        // 使用链表存放样本点
        ArrayList<DataPoint> dp1 = new ArrayList<DataPoint>();
        ArrayList<DataPoint> dp2 = new ArrayList<DataPoint>();
        // 读入样本文件
        session.sendMessage(new TextMessage("读取数据中..."));
        dp1 = readData(filePath1,size1,0);
        dp2 = readData(filePath2,size2,1);
        session.sendMessage(new TextMessage("读取完毕!"));
        /*
         * freq代表了聚类的终止条件，判断还有没有距离小于freq的两个类簇，若有则合并后继续迭代，否则终止迭代
         */
        session.sendMessage(new TextMessage("开始聚类..."));
        List<Cluster> originalClusters1 = initialCluster(dp1);
        List<Cluster> originalClusters2 = initialCluster(dp2);
        List<Cluster> clusters = hc.startCluster(originalClusters2,originalClusters1,N);
        //交换的时候记得换标签label
        //输出聚类的结果，两个类簇中间使用----隔开
        session.sendMessage(new TextMessage("生成聚类结果文件..."));
        String fileName = "DClusteringPosResult" + DateConverter.parseDateString(new Date(),"yyyyMMddHHmmss") +".txt";
        File file = new File(ExpConfig.get("cResultPath") + fileName);
        Writer out = new FileWriter(file);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        Collections.sort(clusters);

        if(clusterSize>clusters.size()){
            clusterSize = clusters.size();
        }

        for (Cluster cl : clusters) {
            List<DataPoint> tempDps = cl.getDataPoints();
            for (DataPoint tempdp : tempDps) {
                String a=tempdp.getDataPointName();
                out.write(a+"\r\n");
            }
            out.write("------------------------------------------------------------------------"+"\r\n");
        }
        out.close();

        //------------------------------------------------------------------------------------------------------------------------
        List<Cluster> sessionList = clusters.subList(0,clusterSize);
        httpSession.setAttribute("DCP",sessionList);
        //填充试验结果对象参数部分
        Experiment experiment = new Experiment();
        ExpResult expResult = new ExpResult();
        expResult.setResultDataType("2");
        experiment.setExpId(dataId);
        expResult.setExperiment(experiment);
        expResult.setResultCreateDate(new Date());
        expResult.setResultIndex(ExpConfig.get("cResultPath") + fileName);
        expResult.setResultMeaning(fileName);
        //------------------------------------------------------------------------------------------------------------------------
        session.sendMessage(new TextMessage("聚类结果文件生成完毕..."));
        return expResult;
    }
    // 聚类的主方法
    private List<Cluster> startCluster(List<Cluster> originalClusters1,List<Cluster> originalClusters2,double N) throws Exception{
        // 声明cluster类，存放类名和类簇中含有的样本
        List<Cluster> Clusters4 = new ArrayList<Cluster>();
        // 初始化类簇，开始时认为每一个样本都是一个类簇并将初始化类簇赋值给最终类簇
        // 声明cluster类，存放类名和类簇中含有的样本
        List<Cluster> finalClusters1 = new ArrayList<Cluster>();
        List<Cluster> finalClusters2 = new ArrayList<Cluster>();
        // 初始化类簇，开始时认为每一个样本都是一个类簇并将初始化类赋值给最终类簇
        finalClusters1 = originalClusters1;
        finalClusters2 = originalClusters2;
        int it = 1;
        //hh.setMin(Double.MIN_VALUE);
        int t;
        int c=0;
        for (int i=0;i<finalClusters1.size();i++) {
            session.sendMessage(new TextMessage("合并相邻元素中"+ " 簇索引位置："+i));
            t=0;
            List<Cluster> Clusters3 = new ArrayList<Cluster>();
            Clusters3.add(setLD(changeCluster(finalClusters1,finalClusters2,N,i)));
            if(Clusters3.get(0).getDataPoints().size()!=0){
                Clusters3.add(setLD(changeCluster1(Clusters3,finalClusters1,N)));
                Clusters3=setLD1(Clusters3);
                Clusters4.add(setLD(Clusters3));//将Ai放入Clusters4
                judgeCluster(Clusters4,c);//合并其中相连的元素
                c++;
                t++;
                i=i-t;
            }
        }
        for(int i=0;i<Clusters4.size();i++) {
            int n=0;
            session.sendMessage(new TextMessage("去除重复元素中"+ " 簇索引位置："+i));
            for(int j=i+1;j<(Clusters4.size()+n);j++) {
                int num=0;
                int count=0;
                int a=getLD(Clusters4,i).size();
                int b=getLD(Clusters4,j-n).size();
                if(a==b) {
                    while(num<a){
                        if(getLD(Clusters4,i).get(num).getDataPointName().equals(getLD(Clusters4,j-n).get(num).getDataPointName())==true)
                        {
                            count++;
                        }
                        num++;
                    }
                    if(count==a) {Clusters4.remove(j-n);n++;}
                }
            }
        }
        return Clusters4;
    }
    private List<Cluster> setLD1(List<Cluster> k) {
        List<Cluster> Clusters = new ArrayList<Cluster>();
        List<DataPoint> dp2 = new ArrayList<DataPoint>();
        for (Cluster cl : k) {
            List<DataPoint> tempDps = cl.getDataPoints();
            for (DataPoint tempdp : tempDps) {
                dp2.add(tempdp);
            }
        }
        Cluster o=new Cluster();
        o.setDataPoints(dp2);
        Clusters.add(o);
        return Clusters;
    }
    private Cluster setLD(List<Cluster> k) {
        List<DataPoint> dp2 = new ArrayList<DataPoint>();
        for (Cluster cl : k) {
            List<DataPoint> tempDps = cl.getDataPoints();
            for (DataPoint tempdp : tempDps) {
                dp2.add(tempdp);
            }
        }
        Cluster o=new Cluster();
        o.setDataPoints(dp2);
        return o;
    }
    private List<DataPoint> getLD(List<Cluster> k,int index) {
        Cluster clusterA = k.get(index);
        List<DataPoint> dp = clusterA.getDataPoints();
        return dp;
    }
    private List<Cluster> changeCluster(List<Cluster> Clusters1,List<Cluster> Clusters2,double N,int i) {
        List<Cluster> Clusters3 = new ArrayList<Cluster>();
        List<DataPoint> dataPointsA = getLD(Clusters1,i);
        int t=0;
        for (int j=0;j<Clusters2.size();j++)
        {

            t=0;
            List<DataPoint> dataPointsB = getLD(Clusters2,j);
            double Dis=0;
            int num=0;
            for(int m=0;m<dataPointsA.size();m++) {
                for (int n=0;n<dataPointsB.size();n++) {
                    if(dataPointsA.get(m).getLabel()!=dataPointsB.get(n).getLabel()) {
                        Dis=Dis+p(dataPointsA.get(m).getDimensioin(), dataPointsB.get(n).getDimensioin());
                        num++;
                    }
                }
            }
            Dis=Dis/num;
            if(Dis>N)
            {
                Clusters3.add(Clusters2.get(j));
                Clusters2.remove(j);
                t++;
                //原先输出有重复，就加了这个;结果就更不相关了
            }
            j=j-t;
        }
        //取第一个序列S1的的第i条，从S2里找出所有与之相关的元素为集合A（只含S2中元素）；
        return Clusters3;
    }
    private List<Cluster> changeCluster1(List<Cluster> Clusters1,List<Cluster> Clusters2,double N) {
        List<Cluster> Clusters3 = new ArrayList<Cluster>();
        List<DataPoint> dataPointsA = getLD(Clusters1,0);
        int t=0;
        for (int j=0;j<Clusters2.size();j++)
        {

            t=0;
            List<DataPoint> dataPointsB = getLD(Clusters2,j);
            double Dis=0;
            int num=0;
            for (int m=0;m<dataPointsA.size();m++) {
                for (int n=0;n<dataPointsB.size();n++) {
                    if(dataPointsA.get(m).getLabel()!=dataPointsB.get(n).getLabel()) {
                        Dis=Dis+p(dataPointsA.get(m).getDimensioin(), dataPointsB.get(n).getDimensioin());
                        num++;
                    }
                }

            }
            Dis=Dis/num;
            if(Dis>N)
            {
                Clusters3.add(Clusters2.get(j));
                Clusters2.remove(j);
                t++;
            }
            j=j-t;
        }
        //从S1找出所有与A相关的元素，并合并为Ai;
        return Clusters3;
    }
    private List<DataPoint> judgeCluster(List<Cluster> k,int index) {
        List<DataPoint> dps = getLD(k,index);
        int q=0;
        for(int i=0;i<dps.size()+q-1;i++)
        {
            if(dps.get(i-q).getLabel()==dps.get(i+1-q).getLabel()){
                if(dps.get(i-q).getNum()+1==dps.get(i-q+1).getNum()) {
                    int r=dps.get(i-q).getNum();
                    int r1=dps.get(i-q).getLabel();
                    enlargeCluster(dps,i-q,dps.get(i-q).getLabel());
                    dps.get(i-q).setNum(r+1);
                    dps.get(i-q).setLabel(r1);
                    q++;
                }
            }
        }

        return dps;
    }
    private List<DataPoint> enlargeCluster(List<DataPoint> dpA, int mergeIndex1,int s) {
        // 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
        DataPoint k=dpA.get(mergeIndex1);
        DataPoint dpt=dpA.get(mergeIndex1+1);
        int m=k.getDimensioinSize()+1;
        double[] temp=new double[m];
        for(int i=0;i<k.getDimensioinSize();i++)
        {
            temp[i]=k.getDimensioin1(i);
        }
        temp[m-1]=dpt.getDimensioin1(dpt.getDimensioinSize()-1);
        String a=Arrays.toString(temp);
        String b=a.substring(1, (a.length()-1));
        if(s==1) {
            DataPoint tempDp = new DataPoint(temp,"<"+b+">");
            dpA.add(mergeIndex1, tempDp);
        }else{
            DataPoint tempDp = new DataPoint(temp,a);
            dpA.add(mergeIndex1, tempDp);
            //System.out.println("呵呵呵");
        }
        dpA.remove(mergeIndex1+1);
        dpA.remove(mergeIndex1+1);

        return dpA;
    }
    public static double co(double[] k1,double[] k2)//协方差E(k1k2)-E(k1)E(k2)
    {
        double a1=ave(k1);//E(k1)
        double a2=ave(k2);//E(k2)
        double co=0.0;
        int l=(k1.length+k2.length)/2;
        double[] k3=new double[l];
        double a3=0;
        for(int i=0;i<l;i++)//E(k1k2)
        {
            k3[i]=k1[i]*k2[i];
            a3+=k3[i];
        }
        a3=a3/l;
        co=a3-(a1*a2);
        return co;
    }
    public static double d(double[] k)//方差
    {
        double a=ave(k);//E(k2)
        double p=0.0;
        int l=k.length;
        double s=0;
        double r=0;
        for(int i=0;i<l;i++)//E(k1k2)
        {
            r=Math.pow(k[i]-a,2);
            s+=r;
        }
        p=s/l;
        return p;
    }
    public static double p(double[] k1,double[] k2)	//相关系数
    {
        double p=0;
        if(Math.abs(co(k1,k2))<0.00001||d(k1)*d(k2)==0) {p=0;}
        else {p=co(k1,k2)/Math.sqrt(d(k1)*d(k2));}
        return p;
    }
    public static double ave(double[] k)//求E()
    {
        double ave=0;
        for(int i=0;i<k.length;i++)
        {
            ave+=k[i];
        }
        ave=ave/k.length;
        return ave;
    }
    public static double[] getData(DataPoint dp1){
        double[] str1 = dp1.getDimensioin();
        return str1;
    }
    public static double[] getMid(List<DataPoint> dp1){
        double l = 0.0;
        double[] str1=null;
        int k=0;
        double[] str2=new double[windowSize];
        if(dp1.size()>1) {k=k+1;}
        for(int i=k;i<dp1.size();i++)
        {
            str1=getData(dp1.get(i));
            if(str1!=null) {
                for(int j=0;j<windowSize;j++)
                {
                    str2[j]=str2[j]+str1[j];
                }
                l++;
            }
        }
        for(int j=0;j<windowSize;j++)
        {
            str2[j]=str2[j]/l;
        }
        return str2;
    }
    // 初始化类簇
    private static List<Cluster> initialCluster(ArrayList<DataPoint> dpoints) {
        // 声明存放初始化类簇的链表
        List<Cluster> originalClusters1 = new ArrayList<Cluster>();
        for (int i=0; i< dpoints.size();i++) {
            // 得到每一个样本点
            DataPoint tempDataPoint = dpoints.get(i);
            // 声明一个临时的用于存放样本点的链表
            List<DataPoint> tempDataPoints = new ArrayList<DataPoint>();
            // 链表中加入刚才得到的样本点
            tempDataPoints.add(tempDataPoint);
            // 声明一个类簇，并且将给类簇设定名字、增加样本点
            Cluster tempCluster = new Cluster();
            tempCluster.setClusterName("Cluster " + String.valueOf(i));
            tempCluster.setDataPoints(tempDataPoints);
            // 将样本点的类簇设置为tempCluster
            tempDataPoint.setCluster(tempCluster);
            // 将新的类簇加入到初始化类簇链表中
            originalClusters1.add(tempCluster);
        }
        return originalClusters1;
    }
    private ArrayList<DataPoint> readData(String path,int size,int s) throws IOException {

        ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
        File file = new File(path);
        if (!file.exists()) {
            session.sendMessage(new TextMessage(("输入文件不存在")));
            System.exit(1);
        }

        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            ArrayList<Double> str =new ArrayList<Double>(size);
            String str1;
            ArrayList<String> q = new ArrayList<String>();
            while ((str1 = br.readLine()) != null) {
                q.add(str1);
                size ++;
            }
            for(int i=0;i<size;i++)
            {
                double a=Double.parseDouble(q.get(i));
                str.add(a);
            }

            double[] b = new double[windowSize];
            double[] g = new double[windowSize];

            for(int i = 0; i<= (size - windowSize); i++) {
                for(int index = 0; index < windowSize; index ++) {
                    b[index]=str.get(i+index);
                }
                g=(double[])b.clone();
                String a=Arrays.toString(g);
                String m=a.substring(1, (a.length()-1));
                if(s==0) {
                    DataPoint k=new DataPoint(g,a);
                    k.setNum(i);
                    k.setLabel(s);
                    dp.add(k);
                }else {
                    DataPoint k = new DataPoint(g,"<"+m+">");
                    k.setNum(i);
                    k.setLabel(s);
                    dp.add(k);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        session.sendMessage(new TextMessage("加载数据完毕，数据大小为：" + dp.size()));
        return dp;
    }
}

