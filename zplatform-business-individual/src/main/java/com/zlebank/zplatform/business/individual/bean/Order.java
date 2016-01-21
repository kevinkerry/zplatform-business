package com.zlebank.zplatform.business.individual.bean;

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
    private String status;
    private String tn;
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
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
    
}
