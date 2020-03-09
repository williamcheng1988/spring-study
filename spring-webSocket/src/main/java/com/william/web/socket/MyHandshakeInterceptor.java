package com.william.web.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //System.out.println("beforeHandshake");
        logger.info("websocket 握手之前");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        //System.out.println("afterHandshake");
        logger.info("websocket 握手之后", ex);
        super.afterHandshake(request, response, wsHandler, ex);
    }
}