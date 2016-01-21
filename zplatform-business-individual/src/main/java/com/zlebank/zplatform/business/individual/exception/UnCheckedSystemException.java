package com.zlebank.zplatform.business.individual.exception;

public class UnCheckedSystemException extends AbstractIndividualBusinessException{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2204249966571087476L;

    @Override
    public String getCode() {
        return "EBUBI0001";
    }

}
