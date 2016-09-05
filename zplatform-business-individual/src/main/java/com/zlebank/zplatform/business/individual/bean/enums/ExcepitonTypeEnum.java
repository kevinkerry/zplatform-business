/* 
 * ExcepitonTypeEnum.java  
 * 
 * version TODO
 *
 * 2016年9月5日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.bean.enums;

import java.net.PasswordAuthentication;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月5日 下午2:53:51
 * @since 
 */
public enum ExcepitonTypeEnum {

	
	MEMBER_ACCOUNT(""),
	MEMBER_INFO(""),
	MEMBER_CARD(""),
	MERCHANTE(""),
	ORDER(""),
	PHONE_SMS(""),
	TRADE(""),
	PASSWORD(""),
	SYSTEM(""),
	UNKNOW("");
	
	private String code;
	
	
	
	/**
	 * @param code
	 */
	private ExcepitonTypeEnum(String code) {
		this.code = code;
	}

	public static ExcepitonTypeEnum fromValue(String code){
		ExcepitonTypeEnum typeEnum = null;
		for(ExcepitonTypeEnum excepitonTypeEnum : values()){
			if(code.equals(excepitonTypeEnum.getCode())){
				typeEnum = excepitonTypeEnum;
			}
		}
		if(typeEnum==null){
			typeEnum = ExcepitonTypeEnum.UNKNOW;
		}
		return typeEnum;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	
	
}
