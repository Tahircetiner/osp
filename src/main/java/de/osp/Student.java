package de.osp;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Student {

    @Id
    private Integer id;

    private String surName;
    private String name;
    private String number;
    private String city;
    private String street;
    private String age;
    private String status;
    private String emailAddress;

    private String grade;
    private String gradeTeacher;

    private String specialNutrition;
    private String physicalImpairment;

    private Boolean isOfLegalAge;

    private String emergencyNumber;
    private String emergencyPerson;

}