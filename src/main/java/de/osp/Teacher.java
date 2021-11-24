package de.osp;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    public Teacher(){

    }
    public Teacher(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
