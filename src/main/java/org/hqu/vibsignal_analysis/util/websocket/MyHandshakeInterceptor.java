package org.hqu.vibsignal_analysis.util.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private Logger logger = LoggerFactory.getLogger(HttpSessionHandshakeInterceptor.class);
    // 握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        System.out
                .println("++++++++++++++++ HandshakeInterceptor: beforeHandshake  ++++++++++++++"
                        + attributes);
        //获取httpsession
//        if(request instanceof ServletServerHttpRequest){
//            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
//            HttpSession httpSession = servletServerHttpRequest.getServletRequest().getSession(false);
//            if(httpSession != null){
//                logger.debug("HttpSessionId:"+httpSession.getId());
//                attributes.put("httpSession", httpSession);
//            }else{
//                logger.debug("httpSession is null");
//            }
//        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    // 握手后
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {

        System.out
                .println("++++++++++++++++ HandshakeInterceptor: afterHandshake  ++++++++++++++");

        super.afterHandshake(request, response, wsHandler, ex);
    }
}