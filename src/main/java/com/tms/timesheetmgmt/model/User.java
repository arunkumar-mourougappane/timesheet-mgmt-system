package com.tms.timesheetmgmt.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "first_name")
    @NotEmpty(message = "*Please provide your name")
    private String firstName;
    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;
    @Column(name = "home_address")
    @NotEmpty(message = "*Please provide your home address")
    private String homeAddress;
    @Column(name = "mobile_number")
    @NotEmpty(message = "*Please provide your mobile contact number")
    @Pattern(regexp = "^\\(\\d{10}\\)|(([\\(]?([0-9]{3})[\\)]?)?[ \\.\\-]?([0-9]{3})[ \\.\\-]([0-9]{4}))$", message = "*Please provide mobile number of an accepted format.")
    private String mobileNumber;
    @Column(name = "active")
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role role;

}
