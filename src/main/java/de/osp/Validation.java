package de.osp;

import java.util.Objects;

public class Validation {
    public Boolean hasAllFieldsFilled(Student student){
        return !Objects.isNull(student);
    }
}
