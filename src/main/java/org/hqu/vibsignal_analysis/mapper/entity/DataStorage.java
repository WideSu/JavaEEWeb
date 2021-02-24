package org.hqu.vibsignal_analysis.mapper.entity;

import org.hqu.vibsignal_analysis.util.DateConverter;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Ricky Zhang
 * @version 1.0 2019-3-18
 */
public class DataStorage {
    private String UUID;

    private String dataId;

    private String userId;

    private String dataName;

    private String dataApplicationRange;

    private String dataClass;

    private String dataIndex;

    private Date uploadDate;

    //此方法用于数据库的循环插入
    private String getUUID() {
        this.UUID = java.util.UUID.randomUUID().toString();
        return UUID;
    }

    private void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataApplicationRange() {
        return dataApplicationRange;
    }

    public void setDataApplicationRange(String dataApplicationRange) {
        this.dataApplicationRange = dataApplicationRange;
    }

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate)throws ParseException {
        DateConverter converter = new DateConverter();
        this.uploadDate = converter.parseDate(uploadDate);
    }
}
