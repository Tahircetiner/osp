package de.osp;

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
        Teacher teacherDb = teacherRepository.findByUsername(teacher.getUsername());

        if(teacherDb == null) {
            return HttpStatus.BAD_REQUEST;
        }

        StringBuilder sb = new StringBuilder();
        try {
            // Apply hash to password.
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(teacher.getPassword().getBytes());
            for (byte datum : data) {
                sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
            }
        } catch(Exception e) {
            return HttpStatus.EXPECTATION_FAILED;
        }

        if(!sb.toString().equals(teacherDb.getPassword())) {
            return HttpStatus.BAD_REQUEST;
        }

        httpSession.setAttribute("username", teacher.getUsername());
        httpSession.setAttribute("hashedpassword", sb);

        return HttpStatus.OK;
    }

    @GetMapping("/onlyteacher")
    public HttpStatus allowOnlyTeacher(HttpSession httpSession){
        if(httpSession.getAttribute("username").equals("tahir")){
            return HttpStatus.OK;
        }else{
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
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
