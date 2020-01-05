package com.tms.timesheetmgmt.controller;

import javax.validation.Valid;

import com.tms.timesheetmgmt.model.Project;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ProjectController
{
   @RequestMapping(value="/add-project", method = RequestMethod.GET)
   public ModelAndView addProjectPage()
   {
       ModelAndView modelAndView = new ModelAndView();
       Project user = new Project();
       modelAndView.addObject("user", user);
       modelAndView.setViewName("add-project");
       log.info("Load Add Project Page.");
       return modelAndView;
   }
   @RequestMapping(value="/add-project", method = RequestMethod.POST)
   public ModelAndView saveProject(@Valid Project project, Authentication authentication, BindingResult bindingResult)
   {
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.addObject("project", project);
       modelAndView.setViewName("add-project");
       log.info("Load Add Project Page.");
       return modelAndView;
   }
}