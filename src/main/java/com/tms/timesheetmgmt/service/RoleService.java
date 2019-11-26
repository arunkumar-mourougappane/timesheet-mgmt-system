package com.tms.timesheetmgmt.service;

import java.util.HashMap;
import java.util.Map;

import com.tms.timesheetmgmt.model.Role;
import com.tms.timesheetmgmt.repository.RoleRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
public class RoleService{

    private RoleRepository roleRepository;

    public Role findByRole (final String role) {
        return roleRepository.findByRole(role);
    }
    
    public Map<Integer,String> getAllRoles()
    {
        Map<Integer,String> allRoles  = new HashMap<Integer,String>();
        List<Role> allRoleList = roleRepository.findAll();
        for (Role roleData : allRoleList)
        {
            allRoles.put(roleData.getId(), roleData.getRole());
        }
        return allRoles;
    }
}