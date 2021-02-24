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

public class HierachicalClusteringNeg {
    static int size1;
    static int size2;
    static int windowSize;
    static int label=1;
    static WebSocketSession session;
    public ExpResult hcn(WebSocketSession webSocketSession,HttpSession httpSession, double correlation, int windowSize,int clusterSize, String filePath1, String filePath2, String dataId)throws Exception {
        this.session = webSocketSession;
        this.windowSize =windowSize;
        double N = correlation;
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
        List<Cluster> clusters = startCluster(dp1,dp2,N);
        //交换的时候记得换标签label
        // 输出聚类的结果，两个类簇中间使用----隔开
        session.sendMessage(new TextMessage("生成聚类结果文件..."));
        String fileName = "HClusteringNegResult" + DateConverter.parseDateString(new Date(),"yyyyMMddHHmmss") +".txt";
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
        httpSession.setAttribute("HCN",sessionList);
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

    //聚类的主方法
    private List<Cluster> startCluster(ArrayList<DataPoint> dp1,ArrayList<DataPoint> dp2,double N) throws IOException {
        // 声明cluster类，存放类名和类簇中含有的样本
        List<Cluster> finalClusters = new ArrayList<Cluster>();
        List<Cluster> finalClusters1 = new ArrayList<Cluster>();
        List<Cluster> finalClusters2 = new ArrayList<Cluster>();
        // 初始化类簇，开始时认为每一个样本都是一个类簇并将初始化类簇赋值给最终类簇
        List<Cluster> originalClusters1 = initialCluster(dp1);
        List<Cluster> originalClusters2 = initialCluster(dp2);
        finalClusters = originalClusters1;
        finalClusters1 = originalClusters1;
        finalClusters2 = originalClusters2;
        // flag为判断标志
        boolean flag = true;
        int it = 1;
        int mergeIndexA = 0;
        int mergeIndexB = 0;
        int ka=0;
        int kb=0;
        while (flag) {
            session.sendMessage(new TextMessage("第"+it+"次迭代"));
            // 临时表量，存放类簇间余弦相似度的最大值
            double min = Double.MAX_VALUE;
            // mergeIndexA和mergeIndexB表示每一次迭代聚类最小的两个类簇，也就是每一次迭代要合并的两个类簇

            /*
             * 迭代开始，分别去计算每个类簇之间的距离，将距离小的类簇合并
             */
            ka=mergeIndexA;
            kb=mergeIndexB;
            for (int i=0;i<finalClusters1.size();i++) {
                session.sendMessage(new TextMessage("第"+it+"次迭代 簇索引位置："+i));
                for (int j=0;j<finalClusters2.size();j++) {
                    // 得到任意的两个类簇
                    Cluster clusterA = finalClusters1.get(i);
                    Cluster clusterB = finalClusters2.get(j);
                    // 得到这两个类簇中的样本
                    List<DataPoint> dataPointsA = clusterA.getDataPoints();
                    List<DataPoint> dataPointsB = clusterB.getDataPoints();
                    if(dataPointsA.equals(dataPointsB)!=true) {
                        /*
                         * 定义临时变量tempDis存储两个类簇的大小
                         */
                        double tempDis = 0;
                        int t=0;
                        for (int m=0;m<dataPointsA.size();m++) {
                            for (int n=0;n<dataPointsB.size();n++) {
                                if(dataPointsA.get(m).getLabel()!=dataPointsB.get(n).getLabel()) {
                                    tempDis=tempDis+p(dataPointsA.get(m).getDimensioin(), dataPointsB.get(n).getDimensioin());
                                    t++;
                                }
                            }

                        }
                        tempDis=tempDis/t;

                        if (tempDis <= min) {
                            min = tempDis;
                            mergeIndexA = i;
                            mergeIndexB = j;
                        }//合并的位置
                    }
                }
            }
            int t1=0;
            int t2=0;//清理重复的簇
            if(it!=1) {
                for(int i=0;i<finalClusters2.size();i++) {
                    int num=0; int count=0;
                    int a=getLD(finalClusters2,i).size();
                    int b=getLD(finalClusters1,ka).size();
                    if(a==b&&i!=mergeIndexB) {
                        while(num<a){
                            if(getLD(finalClusters2,i).get(num).getDataPointName().equals(getLD(finalClusters1,ka).get(num).getDataPointName())==true)
                            {
                                count++;
                            }
                            num++;
                        }
                        if(count==a) {
                            if(i<mergeIndexB) {
                                mergeIndexB--;
                            }
                            if(i<=kb&&kb!=0) {
                                kb--;
                            }
                            finalClusters2.remove(i);
                            t1++;
                            i=i-t1;
                        }
                    }
                }
                for(int i=0;i<finalClusters1.size();i++) {
                    if(finalClusters2.size()==0) {break;}
                    int num=0; int count=0;
                    int a=getLD(finalClusters1,i).size();
                    int b=getLD(finalClusters2,kb).size();
                    if(a==b&&i!=mergeIndexA) {
                        while(num<a){
                            if(getLD(finalClusters1,i).get(num).getDataPointName().equals(getLD(finalClusters2,kb).get(num).getDataPointName())==true)
                            {
                                count++;
                            }
                            num++;
                        }
                        if(count==a) {
                            if(i<mergeIndexA) {
                                mergeIndexA--;
                            }
                            if(i<=ka) {
                                ka--;
                            }
                            finalClusters1.remove(i);
                            t2++;
                            i=i-t2;
                        }
                    }
                }
            }
            if (min>-N) {
                flag = false;
            } else {
                finalClusters = mergeCluster(finalClusters1,finalClusters2,mergeIndexA, mergeIndexB);
            }
            it++;
        }
        if(finalClusters2.size()!=0) {
            for(int i=0;i<finalClusters2.size();i++) {
                finalClusters.add(finalClusters2.get(i));
            }
        }
        List<Cluster> clusters = new ArrayList<Cluster>();
        for(int i=0;i<finalClusters.size();i++)
        {
            clusters.add(judgeCluster(finalClusters,i));
        }
        return clusters;
    }
    private List<Cluster> mergeCluster(List<Cluster> finalClusters1, List<Cluster> finalClusters2,int mergeIndexA, int mergeIndexB) {//合并簇
        // 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
        Cluster clusterA = finalClusters1.get(mergeIndexA);
        Cluster clusterB = finalClusters2.get(mergeIndexB);
        List<DataPoint> dpA = clusterA.getDataPoints();
        List<DataPoint> dpB = clusterB.getDataPoints();
        for (DataPoint dp : dpB) {
            DataPoint tempDp = new DataPoint();
            tempDp.setDataPointName(dp.getDataPointName());
            tempDp.setDimensioin(dp.getDimensioin());
            tempDp.setLabel(dp.getLabel());
            tempDp.setNum(dp.getNum());
            tempDp.setCluster(clusterA);
            dpA.add(tempDp);
        }
        clusterA.setDataPoints(dpA);
        clusterB.setDataPoints(dpA);
        return finalClusters1;
    }
    private List<DataPoint> getLD(List<Cluster> k,int index) {
        Cluster clusterA = k.get(index);
        List<DataPoint> dp = clusterA.getDataPoints();
        return dp;
    }
    private Cluster judgeCluster(List<Cluster> k,int index) {//整合簇内部
        List<DataPoint> dps = getLD(k,index);
        for(int i=0;i<dps.size();i++)
        {
            int t1=i;
            int q=0;
            for(int j=i;j<dps.size()+q;j++) {
                int t2=j-q;
                if((t1!=t2)&&(dps.get(t1).getLabel()==dps.get(t2).getLabel())&&(Math.abs(dps.get(t1).getNum()-dps.get(t2).getNum())==1)){
                    if(dps.get(t1).getNum()<dps.get(t2).getNum()) {
                        int r=dps.get(t2).getNum();
                        int r1=dps.get(t1).getLabel();
                        enlargeCluster(dps,t1,t2,dps.get(t1).getLabel());
                        dps.get(t1).setNum(r);
                        dps.get(t1).setLabel(r1);
                        q++;
                    }else {
                        int r=dps.get(t2).getNum();
                        int r1=dps.get(t2).getLabel();
                        enlargeCluster(dps,t2,t1,dps.get(t2).getLabel());
                        dps.get(t1).setNum(r);
                        dps.get(t1).setLabel(r1);
                        q++;
                    }
                }
            }
        }
        Cluster o=new Cluster();
        o.setDataPoints(dps);
        return o;
    }
    private List<DataPoint> enlargeCluster(List<DataPoint> dpA, int mergeIndex1,int mergeIndex2,int s) {//延长簇
        // 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
        DataPoint k=dpA.get(mergeIndex1);
        DataPoint dpt=dpA.get(mergeIndex2);
        int n=dpt.getDimensioinSize()-k.getDimensioinSize();
        int m=0;
        if(k.getDimensioinSize()>dpt.getDimensioinSize()) {m=k.getDimensioinSize()+1;}
        else {m=k.getDimensioinSize()+1+n;}
        double[] temp=new double[m];
        for(int i=0;i<k.getDimensioinSize();i++)
        {
            temp[i]=k.getDimensioin1(i);
        }
        for(int i=k.getDimensioinSize();i<m;i++) {
            temp[i]=dpt.getDimensioin1(i-k.getDimensioinSize()+windowSize-1);
        }
        String a=Arrays.toString(temp);
        String b=a.substring(1, (a.length()-1));
        int n1=0;
        int n2=0;
        if(mergeIndex1<mergeIndex2) {n1=mergeIndex1;n2=mergeIndex2;}
        else {n1=mergeIndex2;n2=mergeIndex1;}
        if(s==1) {
            DataPoint tempDp = new DataPoint(temp,"<"+b+">");
            dpA.add(n1, tempDp);
        }else{
            DataPoint tempDp = new DataPoint(temp,a);
            dpA.add(n1, tempDp);
            //System.out.println("呵呵呵");
        }
        dpA.remove(n1+1);
        dpA.remove(n2);

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
            s=s+r;
        }
        p=s/l;
        return p;
    }
    public static double p(double[] k1,double[] k2)	//相关系数
    {
        double p=0;
        double k=d(k1)*d(k2);
        if(k==0) {p=0;}
        else {p=co(k1,k2)/Math.sqrt(k);}
        //System.out.println(p);
        if(p>1) {p=1;}
        return p;
    }
    public static double ave(double[] k)//求E()
    {
        double ave=0;
        for(int i=0;i<k.length;i++)
        {
            ave=ave+k[i];
        }
        ave=ave/k.length;
        return ave;
    }
    public static double[] getData(DataPoint dp1){
        double[] str1 = dp1.getDimensioin();
        return str1;
    }
    //初始化类簇
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
            session.sendMessage(new TextMessage("输入文件不存在"));
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
