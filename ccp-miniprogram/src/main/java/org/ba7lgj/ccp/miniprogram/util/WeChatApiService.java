package org.ba7lgj.ccp.miniprogram.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 微信小程序 API 工具。
 */
@Service
public class WeChatApiService
{
    private static final Logger log = LoggerFactory.getLogger(WeChatApiService.class);

    @Value("${ccp.wx.appid:}")
    private String appId;

    @Value("${ccp.wx.secret:}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenId(String code)
    {
        if (StringUtils.isBlank(code))
        {
            throw new IllegalArgumentException("code is empty");
        }
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, secret, code);
        String resp = restTemplate.getForObject(url, String.class);
        log.info("jscode2session response: {}", resp);
        JSONObject json = JSON.parseObject(resp);
        if (json == null)
        {
            throw new IllegalStateException("cannot parse wechat response");
        }
        if (json.containsKey("errcode") && json.getInteger("errcode") != 0)
        {
            throw new IllegalStateException("wechat error: " + json.getString("errmsg"));
        }
        return json.getString("openid");
    }
}
