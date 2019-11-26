package com.tms.timesheetmgmt.repository;

import java.util.List;

import com.tms.timesheetmgmt.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

    List<Role> findAll();
}
