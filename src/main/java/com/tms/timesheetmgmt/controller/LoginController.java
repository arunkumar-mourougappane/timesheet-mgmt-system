package com.tms.timesheetmgmt.controller;

import java.util.Collection;

import javax.validation.Valid;

import com.tms.timesheetmgmt.Dao.UserDao;
import com.tms.timesheetmgmt.model.User;
import com.tms.timesheetmgmt.service.RoleService;
import com.tms.timesheetmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value="/")
    public ModelAndView home(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("title_message","Timesheet Management System");
        if(authentication != null)
        {
            if(authentication.isAuthenticated())
            {
                User user = userService.findUserByEmail(authentication.getName());
                modelAndView.addObject("user",user);
                modelAndView.addObject("homepage_display","DASHBOARD");
            }
        }
        return modelAndView;
    }


    @RequestMapping(value="/add-employee", method = RequestMethod.GET)
    public ModelAndView employeRegPage()
    {
        ModelAndView modelAndView = new ModelAndView();
        UserDao user = new UserDao();
        modelAndView.addObject("userRoles", roleService.getAllRoles());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("add-employee");
        return modelAndView;
    }

    @RequestMapping(value = "/add-employee", method = RequestMethod.POST)
    public ModelAndView createNewEmployee(@Valid UserDao user, Authentication authentication, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        User newUser = new User();
        boolean isAdmin = false;
        boolean isManager = false;
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("add-employee");
        }
        else 
        {

            newUser=User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .homeAddress(user.getHomeAddress())
                    .mobileNumber(user.getMobileNumber())
                    .active(0)
                    .role(roleService.findByRole(user.getRole()))
                    .build();
            if(authentication!= null)
            {
               Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
               for (GrantedAuthority grantedAuthority : authorities){
                    if (grantedAuthority.getAuthority().equals("Admin")) {
                        isAdmin = true;
                        break;
                    }else if (grantedAuthority.getAuthority().equals("Manager")) {
                        isManager = true;
                        break;
                    }
                }
               if(isAdmin)
               {
                   newUser.setActive(1);
               }
               else
               {
                   newUser.setActive(0);
               }
            }
            userService.saveUser(newUser);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("userRoles", roleService.getAllRoles());
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("add-employee");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
    @RequestMapping(value="/manager/home", method = RequestMethod.GET)
    public ModelAndView managerHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Manager Role");
        modelAndView.setViewName("manager/home");
        return modelAndView;
    }

}
