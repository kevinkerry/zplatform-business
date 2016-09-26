package com.zlebank.zplatform.business.individual.service.impl;

import java.util.Map;

import com.zlebank.zplatform.business.individual.bean.Order;

interface IOrderValidator {
    static final String RET_CODE = "retCode";
    static final String RET_MESSAGE = "retMsg";
    static final String RET_CODE_SUCCESS ="00";
    static final String RET_MESSAGE_SUCCESS ="sucess";
    public Map<String, String> validateOrder(Order order);
}
