package de.osp;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {


    @Query("SELECT new de.osp.Teacher(t.id, t.username, t.password) FROM Teacher t")
    List<Teacher> getTeachers();

    Teacher findByUsername(String username);
}
