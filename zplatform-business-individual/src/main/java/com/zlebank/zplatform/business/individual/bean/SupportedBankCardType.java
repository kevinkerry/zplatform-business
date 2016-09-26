package com.zlebank.zplatform.business.individual.bean;

import com.zlebank.zplatform.commons.bean.Bean;

public class SupportedBankCardType implements Bean {
    private Bank bank;
    private String cardType;
    public Bank getBank() {
        return bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
