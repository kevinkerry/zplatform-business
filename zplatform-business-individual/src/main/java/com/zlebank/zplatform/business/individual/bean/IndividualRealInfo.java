package com.zlebank.zplatform.business.individual.bean;
/**
 * a bean represent information of individual member to real name authenticate
 * @author yangying
 *
 */
public class IndividualRealInfo {
	/**银行卡号**/
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
	
	/**设备标示**/
	private String devId;
	
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
	/**
	 * @param cardNo
	 * @param cardType
	 * @param customerName
	 * @param certifType
	 * @param certifNo
	 * @param phoneNo
	 * @param cvn2
	 * @param expired
	 */
	public IndividualRealInfo(String cardNo, String cardType,
			String customerName, String certifType, String certifNo,
			String phoneNo, String cvn2, String expired) {
		super();
		this.cardNo = cardNo;
		this.cardType = cardType;
		this.customerName = customerName;
		this.certifType = certifType;
		this.certifNo = certifNo;
		this.phoneNo = phoneNo;
		this.cvn2 = cvn2;
		this.expired = expired;
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
	/**
	 * 
	 */
	public IndividualRealInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the devId
	 */
	public String getDevId() {
		return devId;
	}
	/**
	 * @param devId the devId to set
	 */
	public void setDevId(String devId) {
		this.devId = devId;
	}
	
	
}
