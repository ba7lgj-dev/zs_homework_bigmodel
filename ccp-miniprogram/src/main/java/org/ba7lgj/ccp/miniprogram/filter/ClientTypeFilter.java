package org.ba7lgj.ccp.miniprogram.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ba7lgj.ccp.common.constants.CcpConstants;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 区分客户端类型的过滤器。
 */
@Component
@Order(0)
public class ClientTypeFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String client = request.getHeader(CcpConstants.HEADER_CLIENT);
        if (client != null && CcpConstants.MINIAPP_CLIENT.equalsIgnoreCase(client))
        {
            request.setAttribute("clientType", CcpConstants.MINIAPP_CLIENT);
        }
        else
        {
            request.setAttribute("clientType", CcpConstants.ADMIN_CLIENT);
        }
        filterChain.doFilter(request, response);
    }
}
