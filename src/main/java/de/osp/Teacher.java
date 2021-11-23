package de.osp;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
public class Teacher {

    @Id
    private Integer id;

    private String username;

    private String password;

}
