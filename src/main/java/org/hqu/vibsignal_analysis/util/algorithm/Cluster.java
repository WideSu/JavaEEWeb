package org.hqu.vibsignal_analysis.util.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements Comparable<Cluster>{
    private List<DataPoint> dataPoints = new ArrayList<DataPoint>(); // 类簇中的样本点
    private String clusterName;
    private int size;
    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }
    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    private int getSize(){
        size = this.dataPoints.size();
        return size;
    }
    @Override
    public int compareTo(Cluster cluster){
        //降序
        return cluster.getSize() - this.getSize();
    }
}
