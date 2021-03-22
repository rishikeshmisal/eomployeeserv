package com.paypal.bfs.test.employeeserv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeservTests {

    @InjectMocks
    EmployeeservApplication employeeservApplication;

    @Test
    public void contextLoads() {
    }
}
