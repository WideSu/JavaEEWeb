package org.hqu.vibsignal_analysis.mapper.entity;

import org.hqu.vibsignal_analysis.util.DateConverter;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-3
 */
public class Experiment {
    private String UUID;

    //用户名称+序列号
    private String expId;

    private String expName;

    private String userId;

    private String userName;

    private String authorizedUserId;

    private String expClass;

    private DataStorage data;

    private String dataId;

    private Date expCreateDate;

    private Date expUpdateDate;

    private String expState;

    private String delFlag;

    private ExpParameter expParameter;

    private List<ExpResult> expResult;

    private List<DataStorage> dataStorageList;

    //-----------------------------------------
    private String algorithm;    //Annie添加的属性

    public ExpParameter getExpParameter() {
        return expParameter;
    }

    public void setExpParameter(ExpParameter expParameter) {
        this.expParameter = expParameter;
    }

    public String getUUID(){
        //this.UUID = java.util.UUID.randomUUID().toString();
        return UUID;
    }

    public void setUUID(String UUID){
        this.UUID = UUID;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthorizedUserId() {
        return authorizedUserId;
    }

    public void setAuthorizedUserId(String authorizedUserId) {
        this.authorizedUserId = authorizedUserId;
    }

    public String getExpClass() {
        return expClass;
    }

    public void setExpClass(String expClass) {
        this.expClass = expClass;
    }

    public DataStorage getData() {
        return data;
    }

    public void setData(DataStorage data) {
        this.data = data;
    }

    public Date getExpCreateDate() {
        return expCreateDate;
    }

    public void setExpCreateDate(Date expCreateDate)throws ParseException{
        DateConverter converter = new DateConverter();
        this.expCreateDate = converter.parseDate(expCreateDate);
    }

    public Date getExpUpdateDate() {
        return expUpdateDate;
    }

    public void setExpUpdateDate(Date expUpdateDate) throws ParseException {
        DateConverter converter = new DateConverter();
        this.expUpdateDate = converter.parseDate(expUpdateDate);
    }

    public String getExpState() {
        return expState;
    }

    public void setExpState(String expState) {
        this.expState = expState;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public List<ExpResult> getExpResult() {
        return expResult;
    }

    public void setExpResult(List<ExpResult> expResult) {
        this.expResult = expResult;
    }

    public List<DataStorage> getDataStorageList() {
        return dataStorageList;
    }

    public void setDataStorageList(List<DataStorage> dataStorageList) {
        this.dataStorageList = dataStorageList;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

}
