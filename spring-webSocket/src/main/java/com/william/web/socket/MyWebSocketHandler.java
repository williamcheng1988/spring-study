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
        if (!users.containsKey(userId)) {
            users.put(userId, session);

            super.afterConnectionEstablished(session);

            //System.out.println(userId + " 已上线!");
            logger.info("{} 已上线!", userId);
        }
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

        //Thread.sleep(2000);

        String sendId= (String) session.getAttributes().get(USER_ID);
        //System.out.println(userId + "发来消息>>>" + message.getPayload());
        logger.info("{} 发来消息>>>{}", sendId, message.getPayload());
        sendMessageToUser(sendId, message.getPayload());
    }

    /**
     * 给某个用户发送消息
     *
     * @param sendId
     * @param message
     */
    public void sendMessageToUser(String sendId, String message) {
        for (String receiveId : users.keySet()) {
            if (message.contains("<all>")) {
                sendMsg(sendId, receiveId, message);
            } else {
                if (receiveId.equals(sendId)) {
                    sendMsg(sendId, receiveId, message);
                    break;
                }
            }
        }
    }

    private void sendMsg(String sendId, String receiveId, String msg) {
        try {
            msg = "已处理<" + sendId + ">发来的消息：" + msg;;
            logger.info("回复 {} 消息>>>{}", receiveId, msg);
            users.get(receiveId).sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
