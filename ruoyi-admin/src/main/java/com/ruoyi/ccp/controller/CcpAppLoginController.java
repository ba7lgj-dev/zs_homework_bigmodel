package com.ruoyi.ccp.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.ccp.domain.MiniUser;
import com.ruoyi.ccp.service.IMiniUserService;
import com.ruoyi.ccp.util.WeChatApiService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 小程序登录接口
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ccp/app")
public class CcpAppLoginController
{
    @Autowired
    private WeChatApiService weChatApiService;

    @Autowired
    private IMiniUserService miniUserService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> body)
    {
        String code = body.get("code");
        if (StringUtils.isEmpty(code))
        {
            throw new ServiceException("code不能为空");
        }
        String openId = weChatApiService.getOpenId(code);
        MiniUser miniUser = miniUserService.autoRegister(openId);
        if (miniUser.getStatus() != null && miniUser.getStatus() == 0)
        {
            throw new ServiceException("账号已被禁用");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(miniUser.getId());
        sysUser.setUserName("mini_" + miniUser.getId());
        sysUser.setNickName(StringUtils.isNotEmpty(miniUser.getNickName()) ? miniUser.getNickName() : "miniapp用户");
        sysUser.setStatus("0");
        sysUser.setDelFlag("0");

        LoginUser loginUser = new LoginUser(sysUser, Collections.emptySet(), "miniapp");
        loginUser.setUserId(miniUser.getId());
        String token = tokenService.createToken(loginUser);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", miniUser);
        return AjaxResult.success(data);
    }
}
