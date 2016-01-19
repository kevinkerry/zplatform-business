package com.zlebank.zplatform.business.individual.bean;
/**
 * a bean represent information of individual member to real name authenticate
 * @author yangying
 *
 */
public class IndividualRealInfo {
	private String cardNo;
	/**
	 * 1-借机卡，2-贷记卡
	 */
	private String cardType;
	/**
	 * 持卡人姓名
	 */
	private String customerName;
	/**
	 * 证件类型
	 */
	private String certifType;
	/**
	 * 证件号
	 */
	private String certifNo;
	/**
	 * 银行预留手机号
	 */
	private String phoneNo;
	/**
	 * 信用卡CVN
	 */
	private String cvn2;
	/**
	 * 信用卡有效期
	 */
	private String expired;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCertifType() {
		return certifType;
	}
	public void setCertifType(String certifType) {
		this.certifType = certifType;
	}
	public String getCertifNo() {
		return certifNo;
	}
	public void setCertifNo(String certifNo) {
		this.certifNo = certifNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCvn2() {
		return cvn2;
	}
	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}
	public String getExpired() {
		return expired;
	}
	public void setExpired(String expired) {
		this.expired = expired;
	}
}