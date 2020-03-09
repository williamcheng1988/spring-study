package com.william.web;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zww
 */
public class WebSocketUser {

    private static Map<String, List<WebSocketSession>> userNameWebsession = null;
    private static Map<WebSocketSession, String> webSessionUserName = null;
    private static Map<String, List<TextMessage>> userNameWithOfflineMsg = null;
    private static long websocketId = 100000000L;

    public WebSocketUser() {
        userNameWebsession = new ConcurrentHashMap<String, List<WebSocketSession>>();
        webSessionUserName = new ConcurrentHashMap<WebSocketSession, String>();
        userNameWithOfflineMsg = new ConcurrentHashMap<String, List<TextMessage>>();
    }

    public Map<String, List<WebSocketSession>> getUserNameWebsession() {
        return userNameWebsession;
    }

    public Map<WebSocketSession, String> getWebSessionUserName() {
        return webSessionUserName;
    }

    public String getUserNameWithWebSocketSession(WebSocketSession webSocketSession) {
        return webSessionUserName.get(webSocketSession);
    }

    public static Map<String, List<TextMessage>> getUserNameWithOfflineMsg() {
        return userNameWithOfflineMsg;
    }

    public static void setUserNameWithOfflineMsg(Map<String, List<TextMessage>> userNameWithOfflineMsg) {
        WebSocketUser.userNameWithOfflineMsg = userNameWithOfflineMsg;
    }

    public static void setUserNameWebsession(Map<String, List<WebSocketSession>> userNameWebsession) {
        WebSocketUser.userNameWebsession = userNameWebsession;
    }

    public static void setWebSessionUserName(Map<WebSocketSession, String> webSessionUserName) {
        WebSocketUser.webSessionUserName = webSessionUserName;
    }

    /**
     * 根据昵称拿WebSocketSession
     *
     * @param userName
     * @return
     */
    public List<WebSocketSession> getWebSocketSessionWithUserName(String userName) {
        return userNameWebsession.get(userName);
    }

    /**
     * 移除失效的WebSocketSession
     *
     * @param webSocketSession
     */
    public synchronized void removeWebSocketSession(WebSocketSession webSocketSession) {
        if (webSocketSession == null) {
            return;
        }

        String nickName = webSessionUserName.get(webSocketSession);
        webSessionUserName.remove(webSocketSession);
        List<WebSocketSession> webSessoin = userNameWebsession.get(nickName);
        if (webSessoin == null) {
            return;
        }
        webSessoin.remove(webSocketSession);
    }

    /**
     * 存放离线消息
     *
     * @param userName
     * @param msg
     */
    public synchronized void putOfflineMsg(String userName, TextMessage msg) {
        if (userNameWithOfflineMsg.get(userName) == null) {
            List<TextMessage> msgList = new ArrayList<TextMessage>();
            msgList.add(msg);
            userNameWithOfflineMsg.put(userName, msgList);
        } else {
            List<TextMessage> msgList = userNameWithOfflineMsg.get(userName);
            msgList.add(msg);
        }
    }

    /**
     * 根据昵称拿离线消息
     *
     * @param userName
     * @return
     */
    public List<TextMessage> getOfflineMsgWithUserName(String userName) {
        return userNameWithOfflineMsg.get(userName);
    }

    /**
     * 存放昵称和WebSocketSession
     *
     * @param userName
     * @param webSocketSession
     */
    public synchronized void putUserNameAndWebSocketSession(String userName, WebSocketSession webSocketSession) {
        webSessionUserName.put(webSocketSession, userName);
        if (userNameWebsession.get(userName) == null) {
            List<WebSocketSession> webSession = new ArrayList<WebSocketSession>();
            webSession.add(webSocketSession);
            userNameWebsession.put(userName, webSession);
        } else {
            List<WebSocketSession> webSession = userNameWebsession.get(userName);
            webSession.add(webSocketSession);
        }
    }

    /**
     * 移除失效的WebSocketSession
     */
    public synchronized long getWebsocketId() {
        websocketId += 1;
        return websocketId;
    }

    public static void main(String srga[]) {
        String test = "123456";

        Boolean flag = true;
        if (test == "012345678".substring(1, 7)) {
            flag = false;
        }
        System.out.println(flag);
    }
}