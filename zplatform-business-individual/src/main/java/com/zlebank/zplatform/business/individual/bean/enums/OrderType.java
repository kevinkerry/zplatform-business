package com.zlebank.zplatform.business.individual.bean.enums;

public enum OrderType {
    /**
     * recharge
     */
    RECHARGE("0002"),
    /**
     * consume
     */
    CONSUME("0001"), 
    /**
     * consume
     */
    WITHDRAW("1001"),
    /**
     * unknow
     */
    UNKNOW("");
    private String code;
    private OrderType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static OrderType fromValue(String code) {
        for (OrderType orderType : OrderType.values()) {
            if (code != null && orderType.equals(code)) {
                return orderType;
            }
        }
        return UNKNOW;
    }
}
