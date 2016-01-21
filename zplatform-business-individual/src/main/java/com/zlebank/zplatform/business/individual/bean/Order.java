package com.zlebank.zplatform.business.individual.bean;

import com.zlebank.zplatform.business.individual.bean.enums.OrderType;
import com.zlebank.zplatform.business.individual.bean.enums.PayWay;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;

public class Order extends OrderBean {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6523152318673013574L;
    private PayWay payWay;
    private String memberId;
    private String bindId;
    private OrderType orderType;
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

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }
}
