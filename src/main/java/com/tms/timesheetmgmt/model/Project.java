package com.tms.timesheetmgmt.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.sql.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "project")
public class Project {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "project_id")
   Long projectId;
   
   @NotEmpty(message = "*Please provide an project name")
   @Column(name = "project_name")
   private String projectName;
   
   @NotEmpty(message = "*Please provide an project start date")
   @Column(name = "start_date")
   private Date startDate;
   
   @NotEmpty(message = "*Please provide an project end date")
   @Column(name = "end_date")
   private Date endDate;
   
   @NotEmpty(message = "*Please provide an project budget")
   @Column(name = "project_budget")
   private Float projectBudget;

}