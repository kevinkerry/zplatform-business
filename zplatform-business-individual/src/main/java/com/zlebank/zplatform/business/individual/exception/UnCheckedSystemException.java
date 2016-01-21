package com.zlebank.zplatform.business.individual.exception;

import java.util.ResourceBundle;

public class UnCheckedSystemException extends AbstractIndividualBusinessException{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2204249966571087476L;

    @Override
    public String getCode() {
        return "EBUBI0001";
    }

	/**
	 *
	 * @return
	 */
	@Override
	public ResourceBundle getResourceBundle() {
		// TODO Auto-generated method stub
		return null;
	}

}
