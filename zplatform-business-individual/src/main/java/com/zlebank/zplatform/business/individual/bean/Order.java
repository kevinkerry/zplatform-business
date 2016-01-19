package com.zlebank.zplatform.business.individual.bean;

import java.util.Currency;

import com.zlebank.zplatform.business.individual.bean.enums.OrderStatus;

public class Order{
	/**
	 * 商户订单号
	 */
	private String orderno;
	private Long orderamt;
	/**
	 * 佣金
	 */
	private Long ordercomm;
	private Long orderfee;
	private String ordercommitime;
	private String coopInstiCode;
	private String instiName;
	/**
	 * 商户号
	 */
	private String memberId;
	/**
	 * 商户名称
	 */
	private String memberName;
	/**
	 * 货物名称
	 */
	private String goodsname;
	/**
	 * 数量
	 */
	private Long goodsnum;
	/**
	 * 货物编码
	 */
	private String goodscode;
	/**
	 * 货物描述
	 */
	private String goodsdescr;
	/**
	 * 货物类型
	 */
	private String goodstype;
	/**
	 * 单价
	 */
	private Long goodsprice;
	/**
	 * 同步通知地址
	 */
	private String fronturl;
	/**
	 * 异步通知地址
	 */
	private String backurl;
	/**
	 * 交易流水号
	 */
	private String relatetradetxn;
	private String orderfinshtime;
	/**
	 * 状态(01:初始，订单提交成功，但未支付；02：支付中；03：支付失败；00：支付成功，04：订单失效)
	 */
	private OrderStatus status;
	/**
	 * 交易类型
	 */
	private String txntype;
	/**
	 * 交易子类型
	 */
	private String txnsubtype;
	/**
	 * 产品类型
	 */
	private String biztype;
	/**
	 * 证书ID
	 */
	private String certid;
	/**
	 * 请求保留域
	 */
	private String reqreserved;
	/**
	 * 保留域
	 */
	private String reserved;
	private String tn;
	/**
	 * 订单描述
	 */
	private String orderdesc;
	/**
	 * 支付超时时间
	 */
	private String paytimeout;
	/**
	 * 支付方IP地址
	 */
	private String payerip;
	private Currency currency;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public Long getOrderamt() {
		return orderamt;
	}
	public void setOrderamt(Long orderamt) {
		this.orderamt = orderamt;
	}
	public Long getOrdercomm() {
		return ordercomm;
	}
	public void setOrdercomm(Long ordercomm) {
		this.ordercomm = ordercomm;
	}
	public Long getOrderfee() {
		return orderfee;
	}
	public void setOrderfee(Long orderfee) {
		this.orderfee = orderfee;
	}
	public String getOrdercommitime() {
		return ordercommitime;
	}
	public void setOrdercommitime(String ordercommitime) {
		this.ordercommitime = ordercommitime;
	}
	public String getCoopInstiCode() {
		return coopInstiCode;
	}
	public void setCoopInstiCode(String coopInstiCode) {
		this.coopInstiCode = coopInstiCode;
	}
	public String getInstiName() {
		return instiName;
	}
	public void setInstiName(String instiName) {
		this.instiName = instiName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public Long getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(Long goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getGoodscode() {
		return goodscode;
	}
	public void setGoodscode(String goodscode) {
		this.goodscode = goodscode;
	}
	public String getGoodsdescr() {
		return goodsdescr;
	}
	public void setGoodsdescr(String goodsdescr) {
		this.goodsdescr = goodsdescr;
	}
	public String getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(String goodstype) {
		this.goodstype = goodstype;
	}
	public Long getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(Long goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getFronturl() {
		return fronturl;
	}
	public void setFronturl(String fronturl) {
		this.fronturl = fronturl;
	}
	public String getBackurl() {
		return backurl;
	}
	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}
	public String getRelatetradetxn() {
		return relatetradetxn;
	}
	public void setRelatetradetxn(String relatetradetxn) {
		this.relatetradetxn = relatetradetxn;
	}
	public String getOrderfinshtime() {
		return orderfinshtime;
	}
	public void setOrderfinshtime(String orderfinshtime) {
		this.orderfinshtime = orderfinshtime;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getTxntype() {
		return txntype;
	}
	public void setTxntype(String txntype) {
		this.txntype = txntype;
	}
	public String getTxnsubtype() {
		return txnsubtype;
	}
	public void setTxnsubtype(String txnsubtype) {
		this.txnsubtype = txnsubtype;
	}
	public String getBiztype() {
		return biztype;
	}
	public void setBiztype(String biztype) {
		this.biztype = biztype;
	}
	public String getCertid() {
		return certid;
	}
	public void setCertid(String certid) {
		this.certid = certid;
	}
	public String getReqreserved() {
		return reqreserved;
	}
	public void setReqreserved(String reqreserved) {
		this.reqreserved = reqreserved;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	public String getOrderdesc() {
		return orderdesc;
	}
	public void setOrderdesc(String orderdesc) {
		this.orderdesc = orderdesc;
	}
	public String getPaytimeout() {
		return paytimeout;
	}
	public void setPaytimeout(String paytimeout) {
		this.paytimeout = paytimeout;
	}
	public String getPayerip() {
		return payerip;
	}
	public void setPayerip(String payerip) {
		this.payerip = payerip;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
