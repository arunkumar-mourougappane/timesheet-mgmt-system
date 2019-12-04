package com.tms.timesheetmgmt.service;

import com.tms.timesheetmgmt.model.Role;
import com.tms.timesheetmgmt.model.User;
import com.tms.timesheetmgmt.repository.RoleRepository;
import com.tms.timesheetmgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public User updateEmployee(User employee) {
        User employeeOldData  = findUserByEmail(employee.getEmail());
        if(employeeOldData == null)
        {
            return saveUser(employee);
        }
        else
        {
            String oldPassHashed = employeeOldData.getPassword();
            if(oldPassHashed != null)
            {
                if(employee.getPassword().equals(oldPassHashed))
                {
                    userRepository.save(employee);
                }
                else
                {
                    employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
                }
            }
            else
            {
                employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
            }
            System.out.println("User Updated.");
            return userRepository.save(employee);
        }
    }
    public List<User> findInActiveUsers()
    {
        return userRepository.findByActiveFalse();
    }
    public List<User> findActiveUsers()
    {
        return userRepository.findByActiveTrue();
    }
}