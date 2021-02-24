package org.hqu.vibsignal_analysis.util.algorithm;

public class DataPoint {
    String dataPointName; // 样本点名
    Cluster cluster; // 样本点所属类簇
    double dimensioin[]; // 样本点的维度
    int num;//序号
    int label;//标签
    public DataPoint(){
    }
    public DataPoint(double[] dimensioin,String dataPointName){
        this.dataPointName=dataPointName;
        this.dimensioin=dimensioin;
    }
    public double[] getDimensioin() {
        return dimensioin;
    }
    public int getDimensioinSize() {
        return dimensioin.length;
    }
    public double getDimensioin1(int i) {
        return dimensioin[i];
    }
    public void setDimensioin(double[] dimensioin) {
        this.dimensioin = dimensioin;
    }

    public Cluster getCluster() {
        return cluster;
    }
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    public String getDataPointName() {

        return dataPointName;
    }
    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }
    public void setNum(int num){
        this.num=num;
    }
    public int getNum(){
        return num;
    }
    public void setLabel(int label){
        this.label=label;
    }
    public int getLabel(){
        return label;
    }
}
