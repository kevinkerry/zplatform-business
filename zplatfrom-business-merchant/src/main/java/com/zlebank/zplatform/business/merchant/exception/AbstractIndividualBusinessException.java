package com.zlebank.zplatform.business.merchant.exception;

import java.util.ResourceBundle;

import com.zlebank.zplatform.commons.exception.AbstractDescribeException;

public abstract class AbstractIndividualBusinessException extends AbstractDescribeException{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8486878069466802182L;
    private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("businessexception_des");
    @Override
    public ResourceBundle getResourceBundle() {
        return RESOURCE;
    }
}
