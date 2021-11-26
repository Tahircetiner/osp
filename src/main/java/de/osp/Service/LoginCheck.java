package de.osp.Service;

import de.osp.Teacher;
import de.osp.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginCheck {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private TeacherRepository teacherRepository;

    public boolean Check() throws NoSuchAlgorithmException {
        boolean isGlobalSessionNull = Objects.isNull(httpSession);
        Object user = null;
        Object pw = null;

        if(!isGlobalSessionNull){
            user = httpSession.getAttribute("username");
            pw = httpSession.getAttribute("hashedpassword");
        }
        if(user != null && pw != null) {
            Teacher teacherDb = teacherRepository.findByUsername(user.toString());
            return !(Hasher.hash(teacherDb.getPassword()).equals(pw));
        }
        return false;
    }
}
