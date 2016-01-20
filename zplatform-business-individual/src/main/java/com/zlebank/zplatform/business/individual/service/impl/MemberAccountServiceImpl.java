package com.zlebank.zplatform.business.individual.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.UnCheckedSystemException;
import com.zlebank.zplatform.business.individual.service.MemberAccountService;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.IGateWayService;

@Service("busiMemberAccountServiceImpl")
public class MemberAccountServiceImpl implements MemberAccountService{
    
    @Autowired
    private IGateWayService gateWayService;
    
    @Override
    @Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED)
    public String recharge(Order order)throws TradeException,AbstractIndividualBusinessException {
        OrderBean tradeOrderBean = new OrderBean();
        String[] ingoreProperties = new String[]{};
        BeanUtils.copyProperties(order, tradeOrderBean,ingoreProperties);
        String tn = null;
        try {
            tn = gateWayService.dealWithWapOrder(tradeOrderBean);
        } catch (TradeException e) {
            e.printStackTrace();
        }catch(Exception e){
            throw new UnCheckedSystemException();
        }
        if(tn==null||tn.equals("")){
            throw new UnCheckedSystemException();
        }
        return tn;
    }

    @Override
    public String withdraw(Order order, String payPwd, String smsCode) {
        return null;
    }

    @Override
    public MemberAccountBean queryMemberFuns(String memberId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PagedResult<MemInAndExDetail> queryAccInAndExDetail(String memberId,
            int page,
            int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

   

    
}
