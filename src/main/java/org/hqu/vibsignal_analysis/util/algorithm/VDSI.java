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

public class VDSI {
	//数据总个数
	static int size=0;
	//序列长度
	static int windowSize;
	static WebSocketSession session;
		//文件读取路径
		//结果输出路径
	public ExpResult vdsi(WebSocketSession webSocketSession, HttpSession httpSession, double distance, int windowSize, int clusterSize, String filePath1, String dataId) throws Exception {
		this.session = webSocketSession;
		this.windowSize = windowSize;
		/*
		 * freq代表了聚类的终止条件，判断还有没有距离小于freq的两个类簇，若有则合并后继续迭代，否则终止迭代
		 */
		double freq= distance;
		// 使用链表存放样本点
		ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
		// 读入样本文件
		session.sendMessage(new TextMessage("读取数据中..."));
		dp = readData(filePath1);
		session.sendMessage(new TextMessage("读取完毕!"));

		session.sendMessage(new TextMessage("开始聚类..."));
		List<Cluster> clusters = startCluster(dp, freq);
		// 输出聚类的结果，两个类簇中间使用----隔开
		session.sendMessage(new TextMessage("生成聚类结果文件..."));

		String fileName = "VDSIResult" + DateConverter.parseDateString(new Date(),"yyyyMMddHHmmss") +".txt";
		File file = new File(ExpConfig.get("cResultPath") + fileName);
		if(!file.getParentFile().exists()){
          file.getParentFile().mkdirs();
      	}

		Collections.sort(clusters);
		if(clusterSize>clusters.size()){
			clusterSize = clusters.size();
		}
	  	Writer out = new FileWriter(file);
		for (Cluster cl : clusters) {
			List<DataPoint> tempDps = cl.getDataPoints();
			for (DataPoint tempdp : tempDps) {
				String k=tempdp.getDataPointName();
				k=k.replace(" ", "");
				String a=k.substring(1, (k.length()-1));
				out.write("<"+a+">"+"\r\n");
			}
		out.write("=========================================================================="+"\r\n");
		out.write("\r\n");
		}
		out.close();

		//------------------------------------------------------------------------------------------------------------------------
		//试验结果传回session
		List<Cluster> sessionList = clusters.subList(0,clusterSize);
		httpSession.setAttribute("VDSI",sessionList);
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
	private List<Cluster> startCluster(ArrayList<DataPoint> dp, double N) throws Exception{
		// 声明cluster类，存放类名和类簇中含有的样本
		List<Cluster> finalClusters = new ArrayList<Cluster>();
		// 初始化类簇，开始时认为每一个样本都是一个类簇并将初始化类簇赋值给最终类簇
		List<Cluster> originalClusters = initialCluster(dp);
		finalClusters = originalClusters;
		boolean flag=true;
		int it = 1;
		while (flag) {
			session.sendMessage(new TextMessage("第" + it + "次迭代"));
			double min=Double.MAX_VALUE;
			//double max=-1; 
			// mergeIndexA和mergeIndexB表示每一次迭代聚类最小的两个类簇，也就是每一次迭代要合并的两个类簇
			int mergeIndexA = 0;
			int mergeIndexB = 0;
			/*
			 * 迭代开始，分别去计算每个类簇之间的距离，将距离小的类簇合并
			 */
			for (int i=0;i<finalClusters.size()-1;i++) {
				for (int j=i+1;j<finalClusters.size();j++) {
					// 得到任意的两个类簇
					
					Cluster clusterA = finalClusters.get(i);
					Cluster clusterB = finalClusters.get(j);
					// 得到这两个类簇  中的样本
					List<DataPoint> dataPointsA = clusterA.getDataPoints();
					List<DataPoint> dataPointsB = clusterB.getDataPoints();
					
					/*
					 * 定义临时变量tempDis存储两个类簇的大小，这里采用的计算两个类簇的距离的方法是
					 * 得到两个类簇中所有的样本的距离的和除以两个类簇中的样本数量的积，其中两个样本 之间的距离用的是余弦相似度。
					 * 注意：这个地方的类簇之间的距离可以 换成其他的计算方法
					 */
					double tempDis = 0;
					/*
					 * 此处计算距离可以优化，事先一次性将两两样本点之间的余弦距离计算好存放一个MAP中，
					 * 这个地方使用的时候直接取出来，就不用每次再去计算了，可节省很多时间。
					 * 注意：若是类簇间的距离计算换成了别的方法，也就没有这种优化的说法了
					 */
		 							
					tempDis=getDistance(getMid(dataPointsA), getMid(dataPointsB));
					if(tempDis==0) {tempDis=N-0.01;}
					if(tempDis<N) {
						mergeIndexA = i;
				        mergeIndexB = j;
				        finalClusters = mergeCluster(finalClusters, mergeIndexA, mergeIndexB);
					}
					if (tempDis<min){ 
					    min=tempDis;
					 }
					 
			       }
				}
					if (min>N) {
						flag = false;
					}
					it++; 
                    
		}      
		return finalClusters;
		 }            
		  
			/*
			 * 若是余弦相似度的最大值都小于给定的阈值， 那说明当前的类簇没有再进一步合并的必要了，
			 * 当前的聚类可以作为结果了，否则的话合并余弦相似度值最大的两个类簇，继续进行迭代 注意：这个地方你可以设定别的聚类迭代的结束条件
			 */
			
	private List<Cluster> mergeCluster(List<Cluster> finalClusters, int mergeIndexA, int mergeIndexB) {
		if (mergeIndexA != mergeIndexB) {
			// 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
			Cluster clusterA = finalClusters.get(mergeIndexA);
			Cluster clusterB = finalClusters.get(mergeIndexB);
			List<DataPoint> dpA = clusterA.getDataPoints();
     		List<DataPoint> dpB = clusterB.getDataPoints();
 			for (DataPoint dp : dpB) {
				DataPoint tempDp = new DataPoint();
				tempDp.setDataPointName(dp.getDataPointName());
				tempDp.setCluster(clusterA);
				tempDp.setDimensioin(dp.getDimensioin());
				dpA.add(tempDp);
			}
			clusterA.setDataPoints(dpA);
			finalClusters.remove(mergeIndexB);
		}
		return finalClusters;
	}
 
	public static double getDistance(double[] str1,double[] str2){
		double dis = 0.0;
		double sum=0.0;
		double ave = 0.0;
		double[] str3=new double[windowSize];
		if((str1!=null)&&(str2!=null)) {
			for(int i=0;i<windowSize;i++) {
			
				str3[i]=Math.abs(str1[i]-str2[i]);
				sum=sum+str3[i];
			}
			ave=sum/windowSize;
		}
			for(int i=0;i<windowSize;i++) {
				dis=dis+Math.pow(str3[i]-ave,2);
			}
			dis=Math.sqrt(dis/windowSize);
			return dis;
	}
	public static double[] getData(DataPoint dp1){
		double[] str1 = dp1.getDimensioin();
		return str1;
	}
public static double[] getMid(List<DataPoint> dp1){
		double l = 0.0;
		double[] str1=null;
		double[] str2=new double[windowSize];
		for(int i=0;i<dp1.size();i++)
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
	// 初始化类簇
	private List<Cluster> initialCluster(ArrayList<DataPoint> dpoints) {
		// 声明存放初始化类簇的链表
		List<Cluster> originalClusters = new ArrayList<Cluster>();

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
			originalClusters.add(tempCluster);
		}

		return originalClusters;
	}
	private ArrayList<DataPoint> readData(String path) throws IOException {
		//在前台重复运行的时候，调用的是原先的算法实例，而静态变量size没有清零，导致持续增加出现空指针异常
		size = 0;
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
				 double a = Double.parseDouble(q.get(i));
			     str.add(a);
			}
			double[] b = new double[windowSize];
			double[] g = new double[windowSize];
			for(int i = 0; i< (size - windowSize); i++) {
					
					for(int index = 0; index < windowSize; index ++) {
						b[index]=str.get(i+index);
					}
				g=(double[])b.clone();
				dp.add(new DataPoint(g,Arrays.toString(g)));
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