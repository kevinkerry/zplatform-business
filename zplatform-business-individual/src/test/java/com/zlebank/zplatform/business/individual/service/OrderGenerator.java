package com.zlebank.zplatform.business.individual.service;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.zlebank.zplatform.business.individual.bean.Order;

public abstract class OrderGenerator {
    protected final String version = "v1.0";
    protected final String encoding = "1";// 1-utf-8
   
    protected final String channelType = "08";
    protected final String accessType = "0";
    protected final String coopInstiCode = "300000000000027";
    protected final String individualMemberId = "999999999999999";
    protected final String merchId = "200000000000568";
    protected final String signMethod = "01";// 01-RSA

    public abstract Order generate(boolean isAnonymous);

    protected String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    protected String randomAlphabetic() {
        Random random = new Random();
        int length = random.nextInt(15);
        String name = RandomStringUtils.randomAlphabetic(length);

        return name;
    }
}
