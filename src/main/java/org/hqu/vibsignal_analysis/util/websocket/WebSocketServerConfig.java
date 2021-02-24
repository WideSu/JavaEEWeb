package org.hqu.vibsignal_analysis.util.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketServerConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 添加拦截地址以及相应的websocket消息处理器
        WebSocketHandlerRegistration registration = registry.addHandler(new MyWebSocketHandler(), "/websocket","sockjs/websocket");
        SockJsServiceRegistration sockJS = registration.withSockJS();
        // 添加拦截器
        registration.addInterceptors(new MyHandshakeInterceptor());
    }

}
