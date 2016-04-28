package com.zlebank.zplatform.business.individual.bean.enums;

public enum PayWay {
    /**
     * 
     */
    ACCOUNT("01"),
    /**
     * paying
     */
    QUICK("02"), UNKNOW("");
    private String code;
    private PayWay(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PayWay fromValue(String code) {
        for (PayWay payWay : PayWay.values()) {
            if (code != null && payWay.getCode().equals(code)) {
                return payWay;
            }
        }
        return UNKNOW;
    }
}
