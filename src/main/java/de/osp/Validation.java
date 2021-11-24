package de.osp;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Objects;

@Data
public class Validation {

    @Autowired
    private StudentRepository studentRepository;

    public Boolean hasAllFieldsFilled(Student student){
        Boolean isFieldNotFilled = true;
        if(Objects.isNull(student.getSurName())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getName())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getNumber())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getCity())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getStreet())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getAge())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getStatus())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getEmailAddress())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getGrade())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getGradeTeacher())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getSpecialNutrition())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getPhysicalImpairment())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getIsOfLegalAge())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getEmergencyNumber())){
            isFieldNotFilled = false;
        }
        if(Objects.isNull(student.getEmergencyPerson())){
            isFieldNotFilled = false;
        }
        return isFieldNotFilled;
    }
}
