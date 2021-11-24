package de.osp;

import de.osp.Service.Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/login")
    public HttpStatus authenticate(@RequestBody Teacher teacher, HttpSession httpSession){
        System.out.println("Handle Login!");
        Teacher teacherDb = teacherRepository.findByUsername(teacher.getUsername());

        if(teacherDb == null) {
            return HttpStatus.BAD_REQUEST;
        }
        String pwHash;
        try {
            pwHash = Hasher.hash(teacher.getPassword());
        } catch(Exception e) {
            return HttpStatus.EXPECTATION_FAILED;
        }

        if(!pwHash.equals(teacherDb.getPassword())) {
            return HttpStatus.BAD_REQUEST;
        }

        httpSession.setAttribute("username", teacher.getUsername());
        httpSession.setAttribute("hashedpassword", pwHash);

        return HttpStatus.OK;
    }

    @GetMapping("/intern/overview")
    public HttpStatus allowOnlyTeacher(HttpSession httpSession){
        Object user = httpSession.getAttribute("username");
        Object pw = httpSession.getAttribute("hashedpassword");
        httpSession.removeAttribute("username");
        httpSession.removeAttribute("hashedpassword");
        System.out.println(user);
        System.out.println(pw);

        if(user != null && pw != null){
            Teacher teacherDb = teacherRepository.findByUsername(user.toString());
            try {
                if(Hasher.hash(teacherDb.getPassword()) == pw) {
                    return HttpStatus.OK;
                }
            } catch (NoSuchAlgorithmException e) {
                return HttpStatus.EXPECTATION_FAILED;
            }
        }

        return HttpStatus.METHOD_NOT_ALLOWED;
    }

    @PostMapping("/saveStudent")
    public ValidationMessage saveStudentInformation(@RequestBody Student student){
        Validation validation = new Validation();
        ValidationMessage validationMessage = new ValidationMessage();

        List<Student> studentList = studentRepository.findAllBySurNameAndNameAndNumberAndCityAndStreetAndAgeAndEmailAddressAndGradeAndGradeTeacherAndSpecialNutritionAndPhysicalImpairmentAndIsOfLegalAgeAndEmergencyNumberAndEmergencyPerson(student.getSurName(), student.getName(), student.getNumber(), student.getCity(), student.getStreet(), student.getAge(), student.getEmailAddress(), student.getGrade(), student.getGradeTeacher(), student.getSpecialNutrition(), student.getPhysicalImpairment(), student.getIsOfLegalAge(), student.getEmergencyNumber(), student.getEmergencyPerson());
        if(Boolean.FALSE.equals(validation.hasAllFieldsFilled(student)) || !studentList.isEmpty()){
            validationMessage.setMessage("Sie haben entweder nicht alle Felder ausgefüllt oder Sie haben sich bereits registriert.");
        }
        else{
            validationMessage.setMessage("Sie haben sich erfolgreich an dem Austauschprogramm angemeldet." +
                    " Bitte wenden Sie sich an ihren zuständigen Lehrer mit ihrem ausgefüllten Anmeldeformular.");
            studentRepository.save(student);
        }
        return validationMessage;
    }
}
