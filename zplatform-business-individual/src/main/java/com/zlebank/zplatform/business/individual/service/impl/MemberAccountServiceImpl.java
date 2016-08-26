package com.zlebank.zplatform.business.individual.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.business.individual.bean.MemInAndExDetail;
import com.zlebank.zplatform.business.individual.bean.Order;
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
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.IMemberAccountService;
import com.zlebank.zplatform.rmi.member.IMemberOperationService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.GateWayServiceProxy;
import com.zlebank.zplatform.trade.bean.wap.WapWithdrawBean;

@Service("busiMemberAccountServiceImpl")
public class MemberAccountServiceImpl implements MemberAccountService {

    @Autowired
    private GateWayServiceProxy gateWayService;
    @Autowired
    private IOrderValidator orderValidator;
    @Autowired
    private IMemberOperationService memberOperationServiceImpl;
    @Autowired
    private IMemberService memberServiceImpl;
    @Autowired
    private IMemberAccountService memberAccountServiceImpl;
    @Autowired
    private SMSServiceProxy smsService;
    @Autowired
    private OrderService orderServiceImpl;
    //@Autowired
    //CoopInstiDAO coopInstiDAO;

    @Override
    public String recharge(Order order) throws ValidateOrderException,
            Exception {
        return orderServiceImpl.createOrder(order);
    }

    @Override
    
    public String withdraw(String json, String payPwd)
            throws ValidateOrderException, Exception{
        json = fullNonWalletData(json);
        WapWithdrawBean withdrawBean = JSON.parseObject(json,
                WapWithdrawBean.class);
        Money withDrawAmount = Money.valueOf(new BigDecimal(withdrawBean
                .getAmount()));
        String memberId = withdrawBean.getMemberId();

        PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId,
                MemberType.INDIVIDUAL);

        if (member == null) {// 资金账户不存在
            throw new Exception("资金账户不存在");
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
        	throw new Exception("资金账户不存在");
        }

        if (basicFund.getBalance().minus(withDrawAmount).compareTo(Money.ZERO) < 0) {// 余额不足
        	throw new Exception("资金账户余额不足");
        }

        
            MemberBean memberBean = new MemberBean();
            memberBean.setLoginName(member.getLoginName());
            memberBean.setInstiId(member.getInstiId());
            memberBean.setPhone(member.getPhone());
            memberBean.setPaypwd(payPwd);
            // 校验支付密码
            if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,
                    memberBean)) {
                //throw new PayPwdVerifyFailException();
            	throw new Exception();
            }
        

        String tn = null;
        try {
            tn = gateWayService.withdraw(json);//.withdraw(json);
        } catch (Exception e) {
            e.printStackTrace();
           // throw new UnCheckedSystemException();
            throw new Exception();
        }
        if (tn == null || tn.equals("")) {
           // throw new UnCheckedSystemException();
        	throw new Exception();
        }
        return tn;
    }
    @Override
    public MemberAccountBean queryMemberFuns(String memberId)
            throws Exception {
        MemberBean member = new MemberBean();
        member.setMemberId(memberId);
        MemberAccountBean memberAccount = null;
       
            memberAccount = memberAccountServiceImpl.queryBalance(MemberType.INDIVIDUAL, member, Usage.BASICPAY);
        
        return memberAccount;
    }

    @Override
    public PagedResult<MemInAndExDetail> queryAccInAndExDetail(String memberId,
            int page,
            int pageSize) throws Exception,
            IllegalAccessException {
        MemberBean member = new MemberBean();
        member.setMemberId(memberId);
        PagedResult<MemberBalanceDetailBean> entrys = null;
        
            entrys = memberAccountServiceImpl.queryBalanceDetail(
                    MemberType.INDIVIDUAL, member, page, pageSize);
        
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
