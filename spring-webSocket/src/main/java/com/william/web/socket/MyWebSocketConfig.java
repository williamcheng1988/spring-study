package com.william.web.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MyWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        String websocket_url = "/websocket/socketServer.do";
        registry.addHandler(new MyWebSocketHandler(), websocket_url).addInterceptors(new MyHandshakeInterceptor());

        String sockjs_url = "/sockjs/socketServer.do";
        registry.addHandler(new MyWebSocketHandler(), sockjs_url).addInterceptors(new MyHandshakeInterceptor()).withSockJS();

    }
}