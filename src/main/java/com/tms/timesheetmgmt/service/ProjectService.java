package com.tms.timesheetmgmt.service;

import java.util.List;

import com.tms.timesheetmgmt.model.Project;
import com.tms.timesheetmgmt.model.User;
import com.tms.timesheetmgmt.repository.ProjectRepository;
import com.tms.timesheetmgmt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
   @Autowired
   private ProjectRepository projectRepository;
   @Autowired
   private UserRepository userRepository;

   @Autowired
   public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
      this.projectRepository = projectRepository;
      this.userRepository = userRepository;

   }

   public Project findByProjectId(final Integer id) {
      return projectRepository.findById(id).orElse(null);
   }

   public List<Project> searchProjectByName(final String projectName) {
      return projectRepository.findByProjectNameContaining(projectName);
   }

   public List<Project> getProjectsByEmployee(final Long id)
   {
      User user = userRepository.findById(id).orElse(null);
      if(user == null)
      {
         return null;
      }
      else
      {
         List<Project> projects = user.getProjects();
         return projects;
      }
   }

   public List<Project> getProjectBelowRemainingBudget(final Float budgetAmount)
   {
      List<Project> projects = projectRepository.findByProjectBudgetLessThan(budgetAmount);
      return projects;
   }

   public List<Project> getProjectAboveBudget(final Float budgetAmount)
   {
      List<Project> projects = projectRepository.findByProjectBudgetGreaterThan(budgetAmount);
      return projects;
   }
}