package com.zlebank.zplatform.business.individual.bean;


import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;
import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.commons.bean.Bean;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;


public class Order extends OrderBean implements Bean{


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6523152318673013574L;
    private PayWay payWay;
    private String memberId;
    private String bindId;
    private OrderStatus status;
    private OrderType orderType;
    private String tn;
    /***银行卡信息***/
    private String cardNo;
    private String cardType;
    private String customerNm;
    private String certifTp;
    private String certifId;
    private String phoneNo;
    private String cvn2;
    private String expired;
    private String smsCode;
    private String bankCode;
    private String busiType;
    
    
    /**原交易订单号**/
    private String origOrderId;
    
    
    
    public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public PayWay getPayWay() {
        return payWay;
    }

    public void setPayWay(PayWay payWay) {
        this.payWay = payWay;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

	/**
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * @return the orderType
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the tn
	 */
	public String getTn() {
		return tn;
	}

	/**
	 * @param tn the tn to set
	 */
	public void setTn(String tn) {
		this.tn = tn;
	}

	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the customerNm
	 */
	public String getCustomerNm() {
		return customerNm;
	}

	/**
	 * @param customerNm the customerNm to set
	 */
	public void setCustomerNm(String customerNm) {
		this.customerNm = customerNm;
	}

	/**
	 * @return the certifTp
	 */
	public String getCertifTp() {
		return certifTp;
	}

	/**
	 * @param certifTp the certifTp to set
	 */
	public void setCertifTp(String certifTp) {
		this.certifTp = certifTp;
	}

	/**
	 * @return the certifId
	 */
	public String getCertifId() {
		return certifId;
	}

	/**
	 * @param certifId the certifId to set
	 */
	public void setCertifId(String certifId) {
		this.certifId = certifId;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the cvn2
	 */
	public String getCvn2() {
		return cvn2;
	}

	/**
	 * @param cvn2 the cvn2 to set
	 */
	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	/**
	 * @return the expired
	 */
	public String getExpired() {
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(String expired) {
		this.expired = expired;
	}

	/**
	 * @return the smsCode
	 */
	public String getSmsCode() {
		return smsCode;
	}

	/**
	 * @param smsCode the smsCode to set
	 */
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	/**
	 * @return the origOrderId
	 */
	public String getOrigOrderId() {
		return origOrderId;
	}

	/**
	 * @param origOrderId the origOrderId to set
	 */
	public void setOrigOrderId(String origOrderId) {
		this.origOrderId = origOrderId;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	
}

