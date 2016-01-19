package com.zlebank.zplatform.business.individual.bean.enums;

public enum OrderStatus {
    /**
     * initial，wait to pay
     */
    INIT("01"),
    /**
     * paying
     */
    PAYING("02"),
    /**
     * pay failed
     */
    PAYFAILED("03"),
    /**
     * pay success
     */
    SUCCESS("04"),UNKNOW("");
    private String code;
    private OrderStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static OrderStatus fromValue(String code){
        for(OrderStatus orderStatus:OrderStatus.values()){
            if(code!=null&&orderStatus.equals(code)){
                return orderStatus;
            }
        }
        return UNKNOW;
    }
}
