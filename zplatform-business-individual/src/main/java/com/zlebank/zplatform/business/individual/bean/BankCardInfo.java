package com.zlebank.zplatform.business.individual.bean;

public class BankCardInfo {
	private Bank bank;
	private String bindcardid;
	private IndividualRealInfo bankCardInfo;
	private String status;

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getBindcardid() {
		return bindcardid;
	}

	public void setBindcardid(String bindcardid) {
		this.bindcardid = bindcardid;
	}

	public IndividualRealInfo getBankCardInfo() {
		return bankCardInfo;
	}

	public void setBankCardInfo(IndividualRealInfo bankCardInfo) {
		this.bankCardInfo = bankCardInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
