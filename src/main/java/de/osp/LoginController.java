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
    public void authenticateAdmin(@RequestBody Teacher teacher, HttpSession httpSession){
        var teacherList = teacherRepository.findByUsername(teacher.getUsername());

        System.out.println(teacherList);
        /*for (Teacher teachers: teacherList
             ) {
            System.out.println(teachers.toString());
        }*/
        System.out.println(teacher.toString());

        httpSession.setAttribute("username", teacher.getUsername());
        // Wenn Zeit noch übrig ist, schauen ob es nicht einfacher gemacht werden kann
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(teacher.getPassword().getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<data.length;i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
                httpSession.setAttribute("hashedpassword", sb);
            }
            System.out.println(sb);

        } catch(Exception e) {
            System.out.println(e);
        }
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
