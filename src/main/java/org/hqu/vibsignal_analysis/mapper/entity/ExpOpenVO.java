package org.hqu.vibsignal_analysis.mapper.entity;

import java.util.Date;

public class ExpOpenVO {
	private String expId;
	private String expName;
	private String userId;
	private Date expUpdateDate;
	
	public ExpOpenVO(String expId, String expName, String userId, Date expUpdateDate) {
		super();
		this.expId = expId;
		this.expName = expName;
		this.userId = userId;
		this.expUpdateDate = expUpdateDate;
	}
	public String getExpId() {
		return expId;
	}
	public void setExpId(String expId) {
		this.expId = expId;
	}
	public ExpOpenVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExpOpenVO(String expName, String userId, Date expUpdateDate) {
		super();
		this.expName = expName;
		this.userId = userId;
		this.expUpdateDate = expUpdateDate;
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
	public Date getExpUpdateDate() {
		return expUpdateDate;
	}
	public void setExpUpdateDate(Date expUpdateDate) {
		this.expUpdateDate = expUpdateDate;
	}
	
}
