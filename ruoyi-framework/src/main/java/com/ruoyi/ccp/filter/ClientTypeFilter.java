package com.ruoyi.ccp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ruoyi.common.utils.StringUtils;

/**
 * 根据请求头区分客户端类型
 *
 * @author ruoyi
 */
@Component
public class ClientTypeFilter extends OncePerRequestFilter
{
    public static final String CLIENT_TYPE_KEY = "clientType";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String client = request.getHeader("X-Client");
        if (StringUtils.isNotEmpty(client) && "miniapp".equalsIgnoreCase(client))
        {
            request.setAttribute(CLIENT_TYPE_KEY, "miniapp");
        }
        else
        {
            request.setAttribute(CLIENT_TYPE_KEY, "admin");
        }
        filterChain.doFilter(request, response);
    }
}
