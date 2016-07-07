package com.zlebank.zplatform.business.individual.exception;

public class MerchWhiteListNotExistException extends AbstractIndividualBusinessException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1293998023938542976L;

    @Override
    public String getCode() {
        return "EBUBI0004";
    }
}
