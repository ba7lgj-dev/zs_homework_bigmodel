package com.ruoyi.ccp.util;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;

/**
 * 微信接口调用工具
 *
 * @author ruoyi
 */
@Component
public class WeChatApiService
{
    private static final Logger log = LoggerFactory.getLogger(WeChatApiService.class);

    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Value("${ccp.wx.appid:}")
    private String appId;

    @Value("${ccp.wx.secret:}")
    private String secret;

    /**
     * 调用微信接口通过code获取openid
     *
     * @param code 登录code
     * @return openid
     */
    public String getOpenId(String code)
    {
        if (StringUtils.isEmpty(code))
        {
            throw new ServiceException("code不能为空");
        }
        String url = CODE2SESSION_URL + "?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        try
        {
            String resp = HttpUtils.sendGet(url);
            JSONObject json = JSONObject.parseObject(resp);
            if (json == null)
            {
                throw new ServiceException("微信接口返回为空");
            }
            if (json.containsKey("errcode") && !Objects.equals(json.getLong("errcode"), 0L))
            {
                log.error("微信code2session错误：{}", json.toJSONString());
                throw new ServiceException("微信登录失败:" + json.getString("errmsg"));
            }
            String openId = json.getString("openid");
            if (StringUtils.isEmpty(openId))
            {
                throw new ServiceException("未获取到openid");
            }
            return openId;
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用微信接口异常", e);
            throw new ServiceException("调用微信接口异常");
        }
    }
}
