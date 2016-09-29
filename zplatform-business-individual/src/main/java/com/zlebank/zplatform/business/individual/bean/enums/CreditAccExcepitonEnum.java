package com.zlebank.zplatform.business.individual.bean.enums;
/**
 * 账户改造错误码
 *
 * @author liusm
 * @version
 * @date 2016年9月5日 下午2:53:51
 * @since 
 */
public enum CreditAccExcepitonEnum {
	UNKNOWN("UNKNOWN","未知"),
	
	CA00("CA00","不存在此会员"),
	CA01("CA01","不存在此账户用途"),
	CA02("CA02","账户开户失败"),
	
	
	
	BW00("BW00","字段校验非法"),
	BW01("BW01","订单会员信息有误"),
	BW02("BW02","不存在此交易"),
	BW03("BW03","只允许保证金提取业务"),
	BW04("BW04","提取会员不存在"),
	BW05("BW05","请检查提取会员资金账户是否正常"),
	BW06("BW06","请检查提取会员保证金账户是否正常"),
	BW07("BW07","保证金余额不足"),
	BW08("BW08","此业务只允许企业会员"),
	BW09("BW09","订单保存失败"),
	
	
	
	GW03("GW03","获取商户版本信息错误");
	;

	private String errorCode; // 错误码
	private String errorMsg; // 错误信息

	private CreditAccExcepitonEnum(String errorCode, String errorMsg) {
		this.errorCode =  errorCode;
		this.errorMsg = errorMsg;
	}

	public static String  getMsg(String code){
        CreditAccExcepitonEnum message=CreditAccExcepitonEnum.valueOf(code);
        if(message==null){
        	message=UNKNOWN;
        }
       return message.getErrorMsg();
	};
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	
	
	
}

