/* 
 * RealNameTypeEnum.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.bean.enums;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 下午3:09:17
 * @since 
 */
public enum RealNameTypeEnum {
	/**
     * 
     */
    CARDREALNAME("01"),
    /**
     * 
     */
    PERSONLANDCARDREALNAME("02"), UNKNOW("");
    private String code;
    private RealNameTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static RealNameTypeEnum fromValue(String code) {
        for (RealNameTypeEnum nameTypeEnum : RealNameTypeEnum.values()) {
            if (code != null && nameTypeEnum.equals(code)) {
                return nameTypeEnum;
            }
        }
        return UNKNOW;
    }
}
