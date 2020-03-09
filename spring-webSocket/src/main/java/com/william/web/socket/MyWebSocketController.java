package com.william.web.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MyWebSocketController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        //System.out.println(username + "登录");
        logger.info("{} 登录", username);
        HttpSession session = request.getSession(true);
        session.setAttribute(MyWebSocketHandler.USER_ID, username);
        //response.sendRedirect("/quicksand/jsp/websocket.jsp");
        return new ModelAndView("socket");
    }



    @RequestMapping(value = "/socket", method = RequestMethod.GET)
    public ModelAndView goIndex() {
        ModelAndView mv = new ModelAndView("socket");
        return mv;
    }

}