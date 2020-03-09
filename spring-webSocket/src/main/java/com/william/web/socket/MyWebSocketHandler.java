package com.william.web.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyWebSocketHandler extends AbstractWebSocketHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());


    private static final Map<String, WebSocketSession> users =  new HashMap<String, WebSocketSession>();

    //用户标识
    public static final String USER_ID = "WEBSOCKET_USERID";   //对应监听器从的key


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("已建立连接!");

        String userId = (String) session.getAttributes().get(USER_ID);
        users.put(userId, session);

        super.afterConnectionEstablished(session);

        //System.out.println(userId + " 已上线!");
        logger.info("{} 已上线!", userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {;
        logger.info("已断开连接!");

        String userId= (String) session.getAttributes().get(USER_ID);
        users.remove(userId);
        super.afterConnectionClosed(session, status);

        //System.out.println(userId + " 已下线!");
        logger.info("{} 已下线!", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Thread.sleep(3000);

        String userId= (String) session.getAttributes().get(USER_ID);
        //System.out.println(userId + "发来消息>>>" + message.getPayload());
        logger.info("{} 发来消息>>>{}", userId, message.getPayload());
        sendMessageToUser(userId, new TextMessage("已处理<" + userId + ">发来的消息：" + message.getPayload()));
    }

    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        for (String id : users.keySet()) {
            if (id.equals(userId)) {
                try {
                    if (users.get(id).isOpen()) {
                        //System.out.println("回复" + userId + "消息>>>" + message.getPayload());
                        logger.info("回复 {} 消息>>>{}", userId, message.getPayload());
                        users.get(id).sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
