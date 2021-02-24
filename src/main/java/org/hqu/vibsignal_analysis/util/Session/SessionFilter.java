package org.hqu.vibsignal_analysis.util.Session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class SessionFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        // 登陆url
        String loginUrl = httpRequest.getContextPath() + "/";
        String url = httpRequest.getRequestURI();
        String path = url.substring(url.lastIndexOf("/"));

        // 超时处理，ajax请求超时设置超时状态，页面请求超时则返回提示并重定向
        if (!path.equals("/register")
                && !path.equals("/validateUser")
                && !path.equals("/createUser")
                && !path.equals("/websocket")
                && !path.equals("/") && !path.contains(".")
                && session.getAttribute("userId") == null) {
            // 判断是否为ajax请求
            if (httpRequest.getHeader("x-requested-with") != null
                    && httpRequest.getHeader("x-requested-with")
                    .equalsIgnoreCase("XMLHttpRequest")) {
                httpResponse.addHeader("sessionstatus", "timeOut");
                httpResponse.addHeader("loginPath", loginUrl);
            }else{
                String str = "<script language='javascript'>alert('登录状态过期,请重新登录');"
                        + "window.top.location.href='"
                        + loginUrl
                        + "';</script>";
                response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
                try {
                    PrintWriter writer = response.getWriter();
                    writer.write(str);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
