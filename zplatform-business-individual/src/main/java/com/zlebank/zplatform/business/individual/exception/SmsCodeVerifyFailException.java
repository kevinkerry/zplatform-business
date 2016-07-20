package com.zlebank.zplatform.business.individual.exception;

public class SmsCodeVerifyFailException extends AbstractIndividualBusinessException{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 169871632028174368L;

    @Override
    public String getCode() {
        return "EBUBI0003";
    }
}
