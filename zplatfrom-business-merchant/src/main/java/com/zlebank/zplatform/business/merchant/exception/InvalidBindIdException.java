package com.zlebank.zplatform.business.merchant.exception;

public class InvalidBindIdException extends AbstractIndividualBusinessException{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3894719993890636025L;

    @Override
    public String getCode() {
        return "EBUBI0005";
    }
}
