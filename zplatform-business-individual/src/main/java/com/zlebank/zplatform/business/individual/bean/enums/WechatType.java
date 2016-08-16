package com.zlebank.zplatform.business.individual.bean.enums;

public enum WechatType {
	 APP("01"), /**APP支付*/
	 RQCODE("02"), /**扫码支付*/
	 UNKNOW("");
	 
    private String typeId;
    
	public String getTypeId() {
		return typeId;
	}

	private WechatType(String typeId) {
		this.typeId = typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	 public static WechatType fromValue(String typeId) {
	        for (WechatType type : WechatType.values()) {
	            if (typeId != null && type.getTypeId().equals(typeId)) {
	                return type;
	            }
	        }
	        return UNKNOW;
	    }
}
