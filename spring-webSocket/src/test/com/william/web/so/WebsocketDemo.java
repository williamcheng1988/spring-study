package com.william.web.so;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
 
 
/**
 * 
 * @Description: 给所用户所有终端推送消息
 * @author liuqin 
 * @date 2018年3月19日 下午3:21:31 
 *
 */
//websocket连接URL地址和可被调用配置
@Controller
@ServerEndpoint(value="/websocketDemo/{userId}",configurator = SpringConfigurator.class)
public class WebsocketDemo {
    //日志记录      
    private Logger logger = LoggerFactory.getLogger(WebsocketDemo.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
   
    //记录每个用户下多个终端的连接
    private static Map<Long, Set<WebsocketDemo>> userSocket = new HashMap<>();
 
    //需要session来对用户发送数据, 获取连接特征userId
    private Session session;
    private Long userId;
   
    /**
     * @Title: onOpen
     * @Description: websocekt连接建立时的操作
     * @param @param userId 用户id
     * @param @param session websocket连接的session属性
     * @param @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId,Session session) throws IOException{
        this.session = session;
        this.userId = userId;
        onlineCount++;
        //根据该用户当前是否已经在别的终端登录进行添加操作
        if (userSocket.containsKey(this.userId)) {
            logger.debug("当前用户id:{}已有其他终端登录",this.userId);
            userSocket.get(this.userId).add(this); //增加该用户set中的连接实例
        }else {
            logger.debug("当前用户id:{}第一个终端登录",this.userId);
            Set<WebsocketDemo> addUserSet = new HashSet<>();
            addUserSet.add(this);
            userSocket.put(this.userId, addUserSet);
        }
        logger.info("用户{}登录的终端个数是为{}",userId,userSocket.get(this.userId).size());
        logger.info("当前在线用户数为：{},所有终端个数为：{}",userSocket.size(),onlineCount);
    }
   
    /**
     * @Title: onClose
     * @Description: 连接关闭的操作
     */
    @OnClose
    public void onClose(){
        onlineCount--;
        //移除当前用户终端登录的websocket信息,如果该用户的所有终端都下线了，则删除该用户的记录
        if (userSocket.get(this.userId).size() == 0) {
            userSocket.remove(this.userId);
        }else{
            userSocket.get(this.userId).remove(this);
        }
        logger.info("用户{}登录的终端个数是为{}",this.userId,userSocket.get(this.userId).size());
        logger.info("当前在线用户数为：{},所有终端个数为：{}",userSocket.size(),onlineCount);
    }
   
    /**
     * @Title: onMessage
     * @Description: 收到消息后的操作
     * @param @param message 收到的消息
     * @param @param session 该连接的session属性
     */
    @OnMessage
    public void onMessage(String message, Session session) {    
        logger.info("收到来自用户id为：{}的消息：{}",this.userId,message);
        if(session ==null){  logger.info("session null");}

    }
   
    /**
     * @Title: onError
     * @Description: 连接发生错误时候的操作
     * @param @param session 该连接的session
     * @param @param error 发生的错误
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.debug("用户id为：{}的连接发送错误",this.userId);
        error.printStackTrace();
    }
   
  /**
   * @Title: sendMessageToUser
   * @Description: 发送消息给用户下的所有终端
   * @param @param userId 用户id
   * @param @param message 发送的消息
   * @param @return 发送成功返回true，反则返回false
   */
    public Boolean sendMessageToUser(Long userId,String message){
        if (userSocket.containsKey(userId)) {
            logger.info(" 给用户id为：{}的所有终端发送消息：{}",userId,message);
            for (WebsocketDemo WS : userSocket.get(userId)) {
                logger.info("sessionId为:{}",WS.session.getId());
                try {
                    WS.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info(" 给用户id为：{}发送消息失败",userId);
                    return false;
                }
            }
            return true;
        }
        logger.info("发送错误：当前连接不包含id为：{}的用户",userId);
        return false;
    }
  
}