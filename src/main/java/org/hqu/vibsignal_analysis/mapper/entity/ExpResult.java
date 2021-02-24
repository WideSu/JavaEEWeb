package org.hqu.vibsignal_analysis.mapper.entity;

import org.hqu.vibsignal_analysis.util.DateConverter;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-13
 */

public class ExpResult {
    private String expResultId;

    private Experiment experiment;

    private String resultIndex;

    private String resultMeaning;

    private Date resultCreateDate;

    private String resultDataType;

    private String delFlag;

    public String getExpResultId() {
        return expResultId;
    }

    public void setExpResultId(String expResultId) {
        this.expResultId = expResultId;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public String getResultIndex() {
        return resultIndex;
    }

    public void setResultIndex(String resultIndex) {
        this.resultIndex = resultIndex;
    }

    public String getResultMeaning() {
        return resultMeaning;
    }

    public void setResultMeaning(String resultMeaning) {
        this.resultMeaning = resultMeaning;
    }

    public Date getResultCreateDate() {
        return resultCreateDate;
    }

    public void setResultCreateDate(Date resultCreateDate) throws ParseException {
        DateConverter converter = new DateConverter();
        this.resultCreateDate = converter.parseDate(resultCreateDate);
    }

    public String getResultDataType() {
        return resultDataType;
    }

    public void setResultDataType(String resultDateType) {
        this.resultDataType = resultDateType;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


}
