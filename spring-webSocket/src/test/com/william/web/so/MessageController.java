package com.william.web.so;

import java.io.IOException;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
@RequestMapping("/message")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    //websocket服务层调用类
    @Autowired
    private WSMessageService wsMessageService;
    
 
  //请求入口
    @RequestMapping(value="/TestWS",method=RequestMethod.GET)
    @ResponseBody
    public String TestWS(@RequestParam(value="userId",required=true) Long userId,
        @RequestParam(value="message",required=true) String message){
        logger.debug("收到发送请求，向用户{}的消息：{}",userId,message);
        if(wsMessageService.sendToAllTerminal(userId, message)){
            return "发送成功";
        }else{
            return "发送失败";
        }   
    }
    
    @RequestMapping(value="/test66",method=RequestMethod.GET)
    public ModelAndView test66() throws IOException{
        //return new ModelAndView("/test", null);
        return new ModelAndView("/test66");
    }
    
    @RequestMapping(value="/test88",method=RequestMethod.GET)
    public ModelAndView test88() throws IOException{
        //return new ModelAndView("/test88", null);
        return new ModelAndView("/test88");
    }
}