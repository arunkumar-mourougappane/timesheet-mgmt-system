package com.tms.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class LoginController
{
   @GetMapping(value="/")
   public ModelAndView home(){
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("index");
       modelAndView.addObject("title_message","Timesheet Management System");
       return modelAndView;
   }

}
