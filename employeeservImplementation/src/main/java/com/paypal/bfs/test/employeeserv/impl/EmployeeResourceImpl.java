package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.repository.IEmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

    private final Logger logger = LoggerFactory.getLogger(EmployeeResourceImpl.class);

    @Autowired
    IEmployeeRepository  employeeRepository;

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) throws Exception {

        int idInt = 0;
        try{
            idInt = Integer.parseInt(id);
        }catch (NumberFormatException e){
            logger.error("request employee id is not a number {}",id);
            throw new BadRequestException(String.format("requesting employee id is NAN, id=%s",id));
        }

        if(idInt<=0){
            throw new BadRequestException("Employee id should be a natural number");
        }

        try {
            Optional<EmployeeEntity> employee = employeeRepository.findById(idInt);

            if(!employee.isPresent()){
                throw new BadRequestException(String.format("employee with id %s does not exist",id));
            }

            Employee response = extractEmployeeDTO(employee.get());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (BadRequestException b){
            throw new BadRequestException(b.getMessage());
        }catch(Exception e){
            logger.error("Database exception: {}",e.getMessage());
            throw new Exception("Something went wrong! Error while retrieving data.");
        }

    }

    private Employee extractEmployeeDTO(EmployeeEntity employeeEntity) throws Exception {

        Employee employee = new Employee();

        employee.setId(employeeEntity.getEmployeeId());
        employee.setFirstName(employeeEntity.getFirstName());
        employee.setLastName(employeeEntity.getLastName());

        try {
            employee.setDateOfBirth(employeeEntity.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }catch (Exception e){
            logger.error("date of birth entry is in wrong format {}",employeeEntity.getDateOfBirth());
            throw new Exception("Something went wrong! Data consistency issue.");
        }
        Address address = new Address();
        address.setCity(employeeEntity.getCity());
        address.setCountry(employeeEntity.getCountry());
        address.setLine1(employeeEntity.getAddressLine1());
        address.setLine2(employeeEntity.getAddressLine2());
        address.setZipCode(employeeEntity.getZipCode());
        address.setState(employeeEntity.getState());

        employee.setAddress(address);

        return employee;

    }

    @Override
    public ResponseEntity<Employee> createEmployee(Employee employee) throws Exception {

        try {
            if (employeeRepository.findById(employee.getId()).isPresent()) {
                throw new BadRequestException(String.format("Employee id=%d is already present", employee.getId()));
            }
        }catch (BadRequestException b){
            throw new BadRequestException(b.getMessage());
        }catch(Exception e){
            logger.error("Database exception: {}",e.getMessage());
            throw new Exception("Something went wrong! Error while retrieving data.");
        }
        EmployeeEntity entity = createEntity(employee);

        employeeRepository.save(entity);

        return new ResponseEntity<>(employee, HttpStatus.CREATED);

    }

    private EmployeeEntity createEntity(Employee employee) {

        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setFirstName(nonNullNonEmpty(employee.getFirstName(),"first_name").toString());

        if(employee.getId()<=0){
            throw new BadRequestException("Employee id should be a natural number");
        }

        employeeEntity.setEmployeeId(employee.getId());
        employeeEntity.setLastName(nonNullNonEmpty(employee.getLastName(),"last_name").toString());
        employeeEntity.setAddressLine1(nonNullNonEmpty(employee.getAddress().getLine1(),"address_line1").toString());
        employeeEntity.setAddressLine2(employee.getAddress().getLine2());
        employeeEntity.setCountry(nonNullNonEmpty(employee.getAddress().getCountry(),"country").toString());
        employeeEntity.setZipCode((int)(nonNullNonEmpty(employee.getAddress().getZipCode(),"zip_code")));
        try {
            employeeEntity.setDateOfBirth(LocalDate.parse(employee.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }catch (Exception e){
            throw new BadRequestException("date of birth format is wrong! Please use yyyy/MM/dd");
        }
        employeeEntity.setState(nonNullNonEmpty(employee.getAddress().getState(),"state").toString());
        employeeEntity.setCity(nonNullNonEmpty(employee.getAddress().getCity(),"city").toString());

        return employeeEntity;
    }

    private Object nonNullNonEmpty(Object value,String paramName){

        if(value==null){
            throw new BadRequestException(String.format("%s cannot be null",paramName));
        }

        if(value instanceof String && StringUtils.isEmpty(value)){
            throw new BadRequestException(String.format("%s cannot be empty or null",paramName));
        }

        return value;

    }


}
