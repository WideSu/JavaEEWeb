package org.hqu.vibsignal_analysis.service;

import org.hqu.vibsignal_analysis.util.algorithm.Cluster;
import org.hqu.vibsignal_analysis.util.algorithm.DataPoint;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ClusterService {
	
	
	public LinkedHashMap<String,List<float[]>> getSerial(List<Cluster> clusters){	
		LinkedHashMap<String,List<float[]>> series = new LinkedHashMap<String,List<float[]>>();
		for(int i=0;i<clusters.size();i++) {
			//依次获取簇类中所有簇
			Cluster cluster = clusters.get(i);
			String clusterName = "第"+(i+1)+"个簇";
			List<String> serials = new ArrayList<String>();
			for(DataPoint dp:cluster.getDataPoints()){
				serials.add(dp.getDataPointName());
			}
			List<float[]> serialsArray = new ArrayList<float[]>();
			//将该簇中所有序列去除
			for (String serial : serials) {
				Map<String, Integer> map = getIndex(serial);
				Integer start = map.get("start");
				Integer end = map.get("end");
				//切割该序列 取出[....] <....> 中的数据
				String serialCut = serial.substring(start, end);
				//按,号切割 获取String数组
				String [] serialArray = serialCut.split(",");
				float [] array = new float[serialArray.length];
				//将String数组转换为float数组
				for (int j = 0; j < serialArray.length; j++) {
					array[j] = Float.parseFloat(serialArray[j]);
				}
				
				serialsArray.add(array);
			}
			series.put(clusterName, serialsArray);
		}
		//System.out.println(series.keySet());
		
		return series;
	}

	public Map<String, Integer> getIndex(String serial){
		Integer start = 0;
		Integer end = 0;
		if(serial.indexOf("[")!=-1){
			 start = serial.indexOf("[")+1;
			 end = serial.indexOf("]");
		}else{
			start = serial.indexOf("<")+1;
			 end = serial.indexOf(">");
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("end", end);
		return map;
	}
}
