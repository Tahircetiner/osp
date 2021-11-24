package de.osp;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findAllBySurNameAndNameAndNumberAndCityAndStreetAndAgeAndEmailAddressAndGradeAndGradeTeacherAndSpecialNutritionAndPhysicalImpairmentAndIsOfLegalAgeAndEmergencyNumberAndEmergencyPerson(String surName, String name, String number, String city, String street, String age, String emailAddress, String grade, String gradeTeacher, String specialNutrition, String physicalImpairment, Boolean isOfLegalAge, String emergencyNumber, String emergencyPerson);
}
