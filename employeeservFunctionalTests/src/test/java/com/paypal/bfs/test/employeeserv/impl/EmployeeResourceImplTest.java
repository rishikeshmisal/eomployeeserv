package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.repository.IEmployeeRepository;
import com.paypal.bfs.test.employeeserv.util.TestDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class EmployeeResourceImplTest {

    @InjectMocks
    private EmployeeResourceImpl employeeResource;

    @Mock
    private IEmployeeRepository iEmployeeRepository;

    @Before
    public void intializeMockito() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = BadRequestException.class)
    public void getANonExistingEmployee() throws Exception {
        Mockito.when(iEmployeeRepository.findById(123)).thenReturn(Optional.empty());
        ResponseEntity<Employee> responseEntity = employeeResource.employeeGetById("123");
    }

    @Test
    public void createAndGetEmployeeTest() throws Exception {
        Employee employee = TestDataGenerator.generateEmployeeDTO();
        ResponseEntity<Employee> responseEntity = employeeResource.createEmployee(employee);
        Assert.assertEquals("Status code check", HttpStatus.CREATED,responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals("Employee id check",employee.getId(),responseEntity.getBody().getId());

        Mockito.when(iEmployeeRepository.findById(10)).thenReturn(TestDataGenerator.generateEmployeeEntity());
        responseEntity = employeeResource.employeeGetById(employee.getId().toString());
        Assert.assertEquals("Status code check",HttpStatus.OK,responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals("Employee id check",employee.getId(),responseEntity.getBody().getId());
    }

    @Test(expected = BadRequestException.class)
    public void createEmployeeDOBExceptionTest() throws Exception {
        Employee employee = TestDataGenerator.generateEmployeeDTO();
        employee.setDateOfBirth("2000-01-01");
        ResponseEntity<Employee> responseEntity = employeeResource.createEmployee(employee);
    }

    @Test(expected = BadRequestException.class)
    public void getEmployeeIdException1() throws Exception {
        ResponseEntity<Employee> responseEntity = employeeResource.employeeGetById("one");
    }

    @Test(expected = BadRequestException.class)
    public void getEmployeeIdException2() throws Exception {
        ResponseEntity<Employee> responseEntity = employeeResource.employeeGetById("100000000000");
    }

    @Test(expected = BadRequestException.class)
    public void getEmployeeIdException3() throws Exception {
        ResponseEntity<Employee> responseEntity = employeeResource.employeeGetById("-1");
    }

    @Test(expected = Exception.class)
    public void getEmployeeIdException4() throws Exception {
        Employee employee = TestDataGenerator.generateEmployeeDTO();
        employee.setDateOfBirth("2000-01-01");
        Mockito.when(iEmployeeRepository.findById(1)).thenThrow(new Exception("Something went wrong"));
        ResponseEntity<Employee> responseEntity = employeeResource.employeeGetById("1");
    }

    @Test(expected = BadRequestException.class)
    public void createEmployeeFailTest() throws Exception {
        Employee employee = TestDataGenerator.generateEmployeeDTO();
        employee.setFirstName("");
        ResponseEntity<Employee> responseEntity = employeeResource.createEmployee(employee);
    }

    @Test(expected = BadRequestException.class)
    public void createEmployeeFailTest2() throws Exception {
        Employee employee = TestDataGenerator.generateEmployeeDTO();
        employee.getAddress().setZipCode(null);
        ResponseEntity<Employee> responseEntity = employeeResource.createEmployee(employee);
    }

}
