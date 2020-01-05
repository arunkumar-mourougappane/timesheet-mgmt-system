package com.tms.timesheetmgmt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tms.timesheetmgmt.controller.LoginController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TimesheetMgmtAppTests
{

    @Autowired
    private LoginController logincontroller;
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(logincontroller);
    }

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/",String.class).contains("Timesheet Management System"));
    }
}
