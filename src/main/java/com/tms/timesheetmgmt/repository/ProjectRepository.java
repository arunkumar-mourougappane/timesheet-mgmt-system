package com.tms.timesheetmgmt.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.tms.timesheetmgmt.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("projectRepository")
public interface ProjectRepository extends JpaRepository<Project, Integer> {

   Optional<Project> findById(Integer id);

   List<Project> findAll();

   List<Project> findByProjectNameContaining(String projectName);

   List<Project> findByProjectBudgetLessThan(Float projectBudget);


   List<Project> findByProjectBudgetGreaterThan(Float projectBudget);

   List<Project> findByEndDate(Date endDate);
}
