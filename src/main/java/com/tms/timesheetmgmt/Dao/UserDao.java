package com.tms.timesheetmgmt.Dao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDao
{

   private Long id;

   @Email(message = "*Please provide a valid Email")
   @NotEmpty(message = "*Please provide an email")
   private String email;

   @Length(min = 5, message = "*Your password must have at least 5 characters")
   @NotEmpty(message = "*Please provide your password")
   private String password;

   @NotEmpty(message = "*Please provide your name")
   private String firstName;
   @NotEmpty(message = "*Please provide your last name")
   private String lastName;
   @NotEmpty(message = "*Please provide your home address")
   private String homeAddress;
   @NotEmpty(message = "*Please provide your mobile contact number")
   @Pattern(regexp = "^\\(\\d{10}\\)|(([\\(]?([0-9]{3})[\\)]?)?[ \\.\\-]?([0-9]{3})[ \\.\\-]([0-9]{4}))$", message = "*Please provide mobile number of an accepted format.")
   private String mobileNumber;
   private int active;
   @NotEmpty(message = "Role Cannot be empty.")
   private String role;

}