package com.tms.timesheetmgmt.service;

import com.tms.timesheetmgmt.model.Role;
import com.tms.timesheetmgmt.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
public class RoleService
{

    private RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role findByRole (final String role) {
        return roleRepository.findByRole(role);
    }
    
    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }
}