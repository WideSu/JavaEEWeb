package org.hqu.vibsignal_analysis.mapper.entity;

import org.hqu.vibsignal_analysis.util.DateConverter;

public class UploadedData {
    DateConverter converter = new DateConverter();
    private String knownPoint;

    private String unKnownPoint;

    private String uploadDate;

    private String p2Predict;

    private String dataId;
    
    private String lp_KnownResponse;
    
    private String lp_Source;
    

    public String getLp_KnownResponse() {
		return lp_KnownResponse;
	}

	public void setLp_KnownResponse(String lp_KnownResponse) {
		this.lp_KnownResponse = lp_KnownResponse;
	}

	public String getLp_Source() {
		return lp_Source;
	}

	public void setLp_Source(String lp_Source) {
		this.lp_Source = lp_Source;
	}

	public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getP2Predict() {
        return p2Predict;
    }

    public void setP2Predict(String p2Predict) {
        this.p2Predict = p2Predict;
    }
}
