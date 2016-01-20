package com.zlebank.zplatform.business.individual.service;

import org.junit.Test;

import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.trade.exception.TradeException;

public class MemberAccountServiceImplTest extends ApplicationContextAbled{
    
    private String coopInstiId;
    private String memberId;
    private String merchId;
    
    
    @Test
    public void test(){
        MemberAccountService memberAccountService = (MemberAccountService)getContext().getBean("busiMemberAccountServiceImpl"); 
        try {
            memberAccountService.recharge(generateOrder());
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AbstractIndividualBusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private Order generateOrder(){
        Order order = new Order();
        return order;
    }
}
