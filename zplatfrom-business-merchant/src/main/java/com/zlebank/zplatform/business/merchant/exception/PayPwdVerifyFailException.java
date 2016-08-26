package com.zlebank.zplatform.business.merchant.exception;

public class PayPwdVerifyFailException extends AbstractIndividualBusinessException{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1182950530518191076L;

    @Override
    public String getCode() {
        return "EBUBI0002";
    }
}
