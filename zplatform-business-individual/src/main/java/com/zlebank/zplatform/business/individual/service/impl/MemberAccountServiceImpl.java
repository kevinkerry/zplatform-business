package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.PayPwdVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.UnCheckedSystemException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.service.MemberAccountService;
import com.zlebank.zplatform.business.individual.service.OrderService;
import com.zlebank.zplatform.business.individual.utils.Constants;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
import com.zlebank.zplatform.member.bean.MemberBalanceDetailBean;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.GetAccountFailedException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.bean.wap.WapWithdrawBean;
import com.zlebank.zplatform.trade.exception.AbstractTradeDescribeException;
import com.zlebank.zplatform.trade.exception.BalanceNotEnoughException;
import com.zlebank.zplatform.trade.exception.FailToGetAccountInfoException;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.IGateWayService;

@Service("busiMemberAccountServiceImpl")
public class MemberAccountServiceImpl implements MemberAccountService {

    @Autowired
    private IGateWayService gateWayService;
    @Autowired
    private IOrderValidator orderValidator;
    @Autowired
    private MemberOperationService memberOperationServiceImpl;
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private com.zlebank.zplatform.member.service.MemberAccountService memberAccountServiceImpl;
    @Autowired
    private ISMSService smsService;
    @Autowired
    private OrderService orderServiceImpl;
    //@Autowired
    //CoopInstiDAO coopInstiDAO;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String recharge(Order order) throws ValidateOrderException,
            TradeException, AbstractIndividualBusinessException {
        return orderServiceImpl.createOrder(order);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String withdraw(String json, String payPwd)
            throws ValidateOrderException, TradeException,
            AbstractTradeDescribeException, AbstractIndividualBusinessException {
        json = fullNonWalletData(json);
        WapWithdrawBean withdrawBean = JSON.parseObject(json,
                WapWithdrawBean.class);
        Money withDrawAmount = Money.valueOf(new BigDecimal(withdrawBean
                .getAmount()));
        String memberId = withdrawBean.getMemberId();

        PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId,
                MemberType.INDIVIDUAL);

        if (member == null) {// 资金账户不存在
            throw new FailToGetAccountInfoException();
        }

        List<BusiAcctQuery> busiAcctList = memberServiceImpl
                .getAllBusiByMId(memberId);
        BusiAcctQuery basicFund = null;
        for (BusiAcctQuery busiAcct : busiAcctList) {
            if (busiAcct.getUsage() == Usage.BASICPAY) {
                basicFund = busiAcct;
                break;
            }
        }

        if (basicFund == null) {// 资金账户不存在
            throw new FailToGetAccountInfoException();
        }

        if (basicFund.getBalance().minus(withDrawAmount).compareTo(Money.ZERO) < 0) {// 余额不足
            throw new BalanceNotEnoughException();
        }

        try {
            MemberBean memberBean = new MemberBean();
            memberBean.setLoginName(member.getLoginName());
            memberBean.setInstiId(member.getInstiId());
            memberBean.setPhone(member.getPhone());
            memberBean.setPaypwd(payPwd);
            // 校验支付密码
            if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,
                    memberBean)) {
                throw new PayPwdVerifyFailException();
            }
        } catch (DataCheckFailedException e) {
            PayPwdVerifyFailException pe = new PayPwdVerifyFailException();
            pe.initCause(e);
            throw pe;
        }

        String tn = null;
        try {
            tn = gateWayService.withdraw(json);//.withdraw(json);
        } catch (TradeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnCheckedSystemException();
        }
        if (tn == null || tn.equals("")) {
            throw new UnCheckedSystemException();
        }
        return tn;
    }
    @Override
    public MemberAccountBean queryMemberFuns(String memberId)
            throws AbstractTradeDescribeException {
        MemberBean member = new MemberBean();
        member.setMemberId(memberId);
        MemberAccountBean memberAccount = null;
        try {
            memberAccount = memberAccountServiceImpl.queryBalance(
                    MemberType.INDIVIDUAL, member, Usage.BASICPAY);
        } catch (DataCheckFailedException e) {
            throw new FailToGetAccountInfoException();
        } catch (GetAccountFailedException e) {
            throw new FailToGetAccountInfoException();
        }
        return memberAccount;
    }

    @Override
    public PagedResult<MemInAndExDetail> queryAccInAndExDetail(String memberId,
            int page,
            int pageSize) throws AbstractTradeDescribeException,
            IllegalAccessException {
        MemberBean member = new MemberBean();
        member.setMemberId(memberId);
        PagedResult<MemberBalanceDetailBean> entrys;
        try {
            entrys = memberAccountServiceImpl.queryBalanceDetail(
                    MemberType.INDIVIDUAL, member, page, pageSize);
        } catch (GetAccountFailedException e) {
            throw new FailToGetAccountInfoException();
        }
        if (entrys == null) {
            return null;
        }
        List<MemInAndExDetail> memInAndExDetailList = new ArrayList<MemInAndExDetail>();
        for (MemberBalanceDetailBean memberBalanceDetailBean : entrys
                .getPagedResult()) {
            MemInAndExDetail memInAndExDetail = new MemInAndExDetail();
            BeanUtils.copyProperties(memberBalanceDetailBean, memInAndExDetail);
            memInAndExDetailList.add(memInAndExDetail);
        }
        PagedResult<MemInAndExDetail> result = new DefaultPageResult<>(
                memInAndExDetailList, entrys.getTotal());
        return result;
    }
    
    private String fullNonWalletData(String jsonStr) {
        /*
         * 钱包接口中没有的参数,但是web收银台接口必须传入的参数
         */
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        jsonObj.put("backUrl", Constants.WALLET_MISSING_FIELD_STR);
        jsonObj.put("virtualId", Constants.WALLET_MISSING_FIELD_STR);
        
        return JSON.toJSONString(jsonObj);
    }
}
