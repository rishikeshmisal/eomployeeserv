package com.paypal.bfs.test.employeeserv.util;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TestDataGenerator {

    public static Employee generateEmployeeDTO(){

        Employee employee = new Employee();
        employee.setDateOfBirth("2000/01/01");
        employee.setFirstName("Sachin");
        employee.setLastName("Tendulkar");
        employee.setId(10);
        Address address = new Address();
        address.setState("MH");
        address.setZipCode(400120);
        address.setLine1("Bandra");
        address.setLine2("Building");
        address.setCountry("IN");
        address.setCity("MUM");
        employee.setAddress(address);

        return employee;

    }

    public static Optional<EmployeeEntity> generateEmployeeEntity(){

        EmployeeEntity employee = new EmployeeEntity();
        employee.setDateOfBirth(LocalDate.now());
        employee.setFirstName("Sachin");
        employee.setLastName("Tendulkar");
        employee.setEmployeeId(10);
        Address address = new Address();
        employee.setState("MH");
        employee.setZipCode(400120);
        employee.setAddressLine1("Bandra");
        employee.setAddressLine2("Building");
        employee.setCountry("IN");
        employee.setCity("MUM");

        return Optional.of(employee);

    }

}
