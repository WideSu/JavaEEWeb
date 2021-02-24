package org.hqu.vibsignal_analysis.mapper.entity;

import org.mybatis.spring.annotation.MapperScan;

import java.util.HashMap;
import java.util.Map;
/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-13
 */
@MapperScan
public class ExpParameter {
    private String UUID;
    //关联实验id
    private Experiment experiment;

    private Map<String, String> feature;

    private String featureName;

    private String featureValue;

    private String delFlag;

    //此方法用于数据库的循环插入
    private String getUUID() {
        this.UUID = java.util.UUID.randomUUID().toString();
        return UUID;
    }

    private void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setFeature(Map<String, String> feature){
        this.freqRangeMin = feature.get("freqRangeMin");
        this.freqRangeMax = feature.get("freqRangeMax");
        this.samplingFreq = feature.get("samplingFreq");
        this.p2Predict = feature.get("p2Predict");
        this.knownPoint = feature.get("knownPoint");
        this.unKnownPoint = feature.get("unKnownPoint");
        this.knownLoad = feature.get("knownLoad");
        this.knownResponse = feature.get("knownResponse");
        this.LP_KnownResponse = feature.get("LP_KnownResponse");
        this.LP_Predict = feature.get("LP_Predict");
        this.LP_Source = feature.get("LP_Source");
        this.data2Predict = feature.get("data2Predict");
        this.algorithm = feature.get("algorithm");
        this.feature = feature;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    //目前作为将众多属性通过mapper存入数据库的这种方法，同时也每个属性也具有各自的get,set方法， 方便存取
    public Map<String, String> getFeature(){
        Map<String, String> map = new HashMap<>();
        map.put("freqRangeMin", freqRangeMin);
        map.put("freqRangeMax", freqRangeMax);
        map.put("samplingFreq", samplingFreq);
        map.put("p2Predict", p2Predict);
        map.put("knownPoint", knownPoint);
        map.put("unKnownPoint", unKnownPoint);
        map.put("knownLoad", knownLoad);
        map.put("knownResponse", knownResponse);
        map.put("LP_KnownResponse", LP_KnownResponse);
        map.put("LP_Predict", LP_Predict);
        map.put("LP_Source", LP_Source);
        map.put("data2Predict", data2Predict);
        map.put("algorithm", algorithm);
        feature = map;
        return feature;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


    //详细的参数属性（便于表单传值）
//----------------------------------------------------------------------
    private String freqRangeMin;

    private String freqRangeMax;

    private String samplingFreq;

    private String p2Predict;

    private String knownPoint;

    private String unKnownPoint;

    private String knownLoad;

    private String knownResponse;

    private String data2Predict;

    private String algorithm;
    
    private String LP_KnownResponse;
    
    private String LP_Source;
    
    private String LP_Predict;

    public String getLP_KnownResponse() {
		return LP_KnownResponse;
	}

	public void setLP_KnownResponse(String lP_KnownResponse) {
		LP_KnownResponse = lP_KnownResponse;
	}

	public String getLP_Source() {
		return LP_Source;
	}

	public void setLP_Source(String lP_Source) {
		LP_Source = lP_Source;
	}

	public String getLP_Predict() {
		return LP_Predict;
	}

	public void setLP_Predict(String lP_Predict) {
		LP_Predict = lP_Predict;
	}

	public String getKnownPoint() {
        return knownPoint;
    }

    public void setKnownPoint(String knownPoint) {
        this.knownPoint = knownPoint;
    }

    public String getUnKnownPoint() {
        return unKnownPoint;
    }

    public void setUnKnownPoint(String unKnownPoint) {
        this.unKnownPoint = unKnownPoint;
    }

    public String getAlgorithm(){
        return algorithm;
    }

    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }

    public String getFreqRangeMin() {
        return freqRangeMin;
    }

    public void setFreqRangeMin(String freqRangeMin) {
        this.freqRangeMin = freqRangeMin;
    }

    public String getFreqRangeMax() {
        return freqRangeMax;
    }

    public void setFreqRangeMax(String freqRangeMax) {
        this.freqRangeMax = freqRangeMax;
    }

    public String getSamplingFreq() {
        return samplingFreq;
    }

    public void setSamplingFreq(String samplingFreq) {
        this.samplingFreq = samplingFreq;
    }

    public String getP2Predict() {
        return p2Predict;
    }

    public void setP2Predict(String p2Predict) {
        this.p2Predict = p2Predict;
    }

    public String getData2Predict() {
        return data2Predict;
    }

    public void setData2Predict(String data2Predict) {
        this.data2Predict = data2Predict;
    }

    public String getKnownLoad() {
        return knownLoad;
    }

    public void setKnownLoad(String knownLoad) {
        this.knownLoad = knownLoad;
    }

    public String getKnownResponse() {
        return knownResponse;
    }

    public void setKnownResponse(String knownResponse) {
        this.knownResponse = knownResponse;
    }
//-------------------------------------------------------------------
}
