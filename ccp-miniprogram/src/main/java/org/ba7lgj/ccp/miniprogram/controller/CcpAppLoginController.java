package org.ba7lgj.ccp.miniprogram.controller;

import java.util.Collections;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.vo.MiniUserVO;
import org.ba7lgj.ccp.core.service.IMiniUserService;
import org.ba7lgj.ccp.miniprogram.util.WeChatApiService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 小程序登录接口。
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

    public static class LoginRequest
    {
        private String code;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginRequest request)
    {
        String openId = weChatApiService.getOpenId(request.getCode());
        MiniUser miniUser = miniUserService.selectMiniUserByOpenId(openId);
        if (miniUser == null)
        {
            miniUser = miniUserService.autoRegisterByOpenId(openId);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(miniUser.getId());
        sysUser.setUserName("ccp_" + miniUser.getId());
        sysUser.setNickName(miniUser.getNickName());

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(miniUser.getId());
        loginUser.setUser(sysUser);
        loginUser.setPermissions(Collections.emptySet());

        String token = tokenService.createToken(loginUser);

        MiniUserVO vo = new MiniUserVO();
        BeanUtils.copyProperties(miniUser, vo);
        return AjaxResult.success().put("token", token).put("user", vo);
    }
}
