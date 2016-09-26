package com.zlebank.zplatform.business.merchant.exception;

public class ValidateOrderException extends Exception{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4563584613045588568L;
    
    private String code;
    private String message;
    
    public ValidateOrderException(String code,String message){
        super();
        this.code = code;
        this.message = message;
    }
    
    public String getCode(){
        return code;
    }
    @Override
    public String getMessage(){
       return message;
    }
}
