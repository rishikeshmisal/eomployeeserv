package com.paypal.bfs.test.employeeserv.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class EmployeeEntity {

    @Id
    @Column(name = "id")
    private Integer employeeId;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "date_of_birth",nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "address_line1",nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "country",nullable = false)
    private String country;

    @Column(name = "state",nullable = false)
    private String state;

    @Column(name = "zip_code",nullable = false)
    private Integer zipCode;
}
