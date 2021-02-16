package com.tms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController
{
   @RequestMapping(value="/")
   public ModelAndView home(){
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("index");
       modelAndView.addObject("title_message","Timesheet Management System");
       return modelAndView;
   }

}
