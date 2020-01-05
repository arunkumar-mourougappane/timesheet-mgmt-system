package com.tms.timesheetmgmt.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        log.info("Login Page Loaded");
        return modelAndView;
    }

    @RequestMapping(value="/")
    public ModelAndView home(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("title_message","Timesheet Management System");
        Boolean isAdmin=false;
        Boolean isManager=false;
        if(authentication != null)
        {
            if(authentication.isAuthenticated())
            {
                log.info("User was already logged in, User Dashboard page will be displayed.");

                User user = userService.findUserByEmail(authentication.getName());
                modelAndView.addObject("user",user);
                modelAndView.addObject("homepage_display","DASHBOARD");

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities)
                {
                    if (grantedAuthority.getAuthority().equals("Admin"))
                    {
                        isAdmin = true;
                        break;
                    }
                    else if (grantedAuthority.getAuthority().equals("Manager"))
                    {
                        isManager = true;
                        break;
                    }
                }
                if(isAdmin)
                {
                    log.info("Populating Admin dashboard information.");
                    List<User> inactiveUsers = userService.findInActiveUsers();
                    modelAndView.addObject("inactiveEmployees", inactiveUsers);
                    List<User> activeUsers = userService.findActiveUsers();
                    modelAndView.addObject("activeEmployees", activeUsers);
                }
            }
        }
        return modelAndView;
    }


    @RequestMapping(value="/add-employee", method = RequestMethod.GET)
    public ModelAndView employeeRegPage()
    {
        ModelAndView modelAndView = new ModelAndView();
        UserDao user = new UserDao();
        modelAndView.addObject("userRoles", roleService.getAllRoles());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("add-employee");
        log.info("Load Add Employee Page.");
        return modelAndView;
    }

    @RequestMapping(value="/forgot-password", method = RequestMethod.GET)
    public ModelAndView forgotPassword(Authentication authentication)
    {
        ModelAndView modelAndView = new ModelAndView();
        String  email = new String();
        if(authentication!= null)
        {           
            if(authentication.isAuthenticated())
            {
                log.info("User already logged in. Getting current user info.");
                email = authentication.getName();
            }
        }
        modelAndView.addObject("email", email);
        modelAndView.setViewName("forgot-password");
        log.info("Loading forgot password page.");
        return modelAndView;
    }

    @RequestMapping(value="/forgot-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@RequestParam("email") String email, @RequestParam("password") String password, Authentication authentication)
    {
        ModelAndView modelAndView = new ModelAndView();
        if(email != null)
        {
            User employee = userService.findUserByEmail(email);
            if(employee != null)
            {
                log.info("Found a user for the given id: %s", email);
                employee.setPassword(password);
                userService.saveUser(employee);
                modelAndView.addObject("email", email);
                modelAndView.addObject("success_message", "Password has been updated Successfully!");
            }
            else
            {
                log.error("Cannot find a user for the given id: %s", email);
                modelAndView.addObject("failure_message", "No user of that id found!");
            }
        }
        else
        {
            log.error("Email id was invalid.");
            modelAndView.addObject("failure_message", "Invalid email entry!");
        }
        modelAndView.setViewName("forgot-password");
        return modelAndView;
    }

    @RequestMapping(value="/edit-employee-by-id", method = RequestMethod.GET)
    public ModelAndView editEmployeePage(@RequestParam("employeeId") Long employeeId, Authentication authentication)
    {
        ModelAndView modelAndView = new ModelAndView();
        boolean isAdmin = false;
        boolean isManager = false;
        if(authentication!= null)
        {
            if(authentication.isAuthenticated())
            {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities)
                {
                    if (grantedAuthority.getAuthority().equals("Admin"))
                    {
                        log.info("Current user is admin, flag set for admin");
                        isAdmin = true;
                        break;
                    }
                    else if (grantedAuthority.getAuthority().equals("Manager"))
                    {
                        log.info("Current user is manager, flag set for admin");
                        isManager = true;
                        break;
                    }
                }
            }
        }
        if(isAdmin)
        {
            log.info("Allowing admin to edit employee info for id: %ld",employeeId);
            UserDao employeeDao = new UserDao();
            User employee = new User();
            employee = userService.findUserById(employeeId);
            if(employee != null)
            {
                log.info("Populating employee data for page");
                employeeDao=UserDao.builder()
                    .id(employeeId)
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .password(employee.getPassword())
                    .homeAddress(employee.getHomeAddress())
                    .mobileNumber(employee.getMobileNumber())
                    .active(employee.isActive())
                    .role(employee.getRole().getRole())
                    .build();
                    modelAndView.addObject("user", employeeDao);
                    modelAndView.addObject("userRoles", roleService.getAllRoles());
                    modelAndView.setViewName("edit-employee");
            }
            else
            {
                log.info("Cannot find employee info for id: %ld",employeeId);
                modelAndView.setViewName("index");
            }
        }
        else
        {
            log.error("No proper Access to edit employee data for page");
            modelAndView.setViewName("access-denied");
        }
        return modelAndView;
    }


    @RequestMapping(value="/access-denied", method = RequestMethod.GET)
    public ModelAndView accessDenied()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("denied_message", "Sorry, You need to be logged in to access this page.");
        modelAndView.setViewName("access-denied");
        return modelAndView;
    }

    @RequestMapping(value = "/edit-employee", method = RequestMethod.POST)
    public ModelAndView updateEmployee(@Valid UserDao user, Authentication authentication, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        boolean isAdmin = false;
        boolean isManager = true;

        if(authentication!= null)
        {
            if(authentication.isAuthenticated())
            {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority grantedAuthority : authorities)
                {
                    if (grantedAuthority.getAuthority().equals("Admin"))
                    {
                        log.info("Current user is admin, flag set for admin");
                        isAdmin = true;
                        break;
                    }
                    else if (grantedAuthority.getAuthority().equals("Manager"))
                    {
                        log.info("Current user is manager, flag set for admin");
                        isManager = true;
                        break;
                    }
                }
            }
        }
        log.info("Building user instance information.");
        User updatedUser=User.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .password(user.getPassword())
            .homeAddress(user.getHomeAddress())
            .mobileNumber(user.getMobileNumber())
            .active(user.isActive())
            .role(roleService.findByRole(user.getRole()))
            .build();
            
        if(isAdmin)
        {
            if(userService.updateEmployee(updatedUser) != null)
            {
                log.error("Failed to update employee information.");
                modelAndView.setViewName("index");
            }
            modelAndView.addObject("status_message", "Successfully Updated details of "+updatedUser.getFirstName());
            modelAndView.addObject("user", user);
            modelAndView.addObject("userRoles", roleService.getAllRoles());
            modelAndView.setViewName("edit-employee");
        }
        modelAndView.addObject("denied_message", "Sorry, You need to be logged in to access this page.");
        modelAndView.setViewName("access-denied");
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
            modelAndView.addObject("message", "There is already a user registered with the email provided");
            log.error("Employee already exists.");
            modelAndView.addObject("userRoles", roleService.getAllRoles());
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("add-employee");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message",bindingResult.getFieldErrors().toString() );
            modelAndView.addObject("user", user);
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
                    .active(false)
                    .role(roleService.findByRole(user.getRole()))
                    .build();
            if(authentication!= null)
            {   
                if(authentication.isAuthenticated() )
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
                }
               if(isAdmin)
               {
                   newUser.setActive(true);
                   userService.saveUser(newUser);
                   modelAndView.addObject("message", "User has been registered successfully and Activated.");
                   modelAndView.addObject("userRoles", roleService.getAllRoles());
                   modelAndView.addObject("user", new User());
                   modelAndView.setViewName("add-employee");
               }
               else if(isManager)
               {
                   newUser.setActive(false);
                   userService.saveUser(newUser);
                   modelAndView.addObject("message", "User has been registered successfully. Email adimin to activate user.");
                   modelAndView.addObject("userRoles", roleService.getAllRoles());
                   modelAndView.addObject("user", new User());
                   modelAndView.setViewName("add-employee");
               }
               else
               {
                   log.info("Current user doesn't have enough access.");
                   modelAndView.addObject("denied_message", "Sorry, You need to be logged in to access this page.");
                   modelAndView.setViewName("access-denied");
               }
            }


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

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView errorPage( Authentication authentication, BindingResult bindingResult) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
