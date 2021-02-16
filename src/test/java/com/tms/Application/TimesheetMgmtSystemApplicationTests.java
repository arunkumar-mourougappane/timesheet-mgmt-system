package com.tms.Application;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tms.Controller.LoginController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TimesheetMgmtSystemApplicationTests {

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
    public void testForHomeRequestMapping() throws Exception {
        assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/",String.class).contains("Timesheet Management System"));
    }

}
