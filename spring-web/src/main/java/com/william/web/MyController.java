package com.william.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView goIndex() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        return mv;
    }

}