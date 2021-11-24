package de.osp;

import lombok.Data;

import java.util.Objects;

@Data
public class Validation {
    public Boolean hasAllFieldsFilled(Student student){
        return !Objects.isNull(student);
    }
}
