package de.osp;
import de.osp.Service.Hasher;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    //private HttpSession globalHttpSession;

    @PostMapping("/login")
    public ValidationMessage authenticate(@RequestBody Teacher teacher, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        System.out.println("Handle Login!");
        Teacher teacherDb = teacherRepository.findByUsername(teacher.getUsername());
        ValidationMessage validationMessage = new ValidationMessage();
        httpServletRequest.getSession(true);
        if(teacherDb == null) {
            validationMessage.setMessage("bla");
        }
        String pwHash = "";
        try {
            pwHash = Hasher.hash(teacher.getPassword());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(!pwHash.equals(teacherDb.getPassword())) {
            //validationMessage.setMessage("blubb");
        }
        else{
            validationMessage.setMessage("erfolg");
        }

        httpServletRequest.setAttribute("username", teacher.getUsername());
        httpServletRequest.setAttribute("hashedpassword", pwHash);
        System.out.println("session" + httpServletRequest.getAttribute("username"));
        return validationMessage;
    }

    @Async
    @GetMapping("/intern/overview.html")
    public void allowOnlyTeacher(HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws NoSuchAlgorithmException, IOException, InterruptedException {
        //Hier existiert irgendein Problem mit der Session
        System.out.println("cookies"  +httpServletRequest.getCookies());
        Boolean isGlobalSessionNull = Objects.isNull(httpSession);
        Object user = null;
        Object pw = null;
        if(!isGlobalSessionNull){
            user = httpSession.getAttribute("username");
            pw = httpSession.getAttribute("hashedpassword");
        }
        else{
            httpSession.invalidate();
            System.out.println(httpServletRequest.getSession());
        }
        ResponseEntity<String> bodyBuilder = null;
        if(user != null && pw != null){
            Teacher teacherDb = teacherRepository.findByUsername(user.toString());
            System.out.println(!(Hasher.hash(teacherDb.getPassword()).equals(pw)));
            if(!(Hasher.hash(teacherDb.getPassword()).equals(pw))) {
                System.out.println("redirecting");
                String overViewPage = new String(Files.readAllBytes(Paths.get("C://Projekte//Schulprojekte//osp//src//main//resources//static//intern//overview" + ".html")), "UTF-8");
                PrintWriter printWriter = httpServletResponse.getWriter();
                printWriter.println(overViewPage);
                printWriter.flush();
                //bodyBuilder = ResponseEntity.ok().body(overViewPage);
            }

        }
        else{
            System.out.println("else");
            httpServletResponse.setHeader("Location", "http://localhost:8080/");
            httpServletResponse.setStatus(302);
            httpServletResponse.setContentType("text/html");
            PrintWriter out = httpServletResponse.getWriter();
            String homePage = new String(Files.readAllBytes(Paths.get("C://Projekte//Schulprojekte//osp//src//main//resources//static//index" + ".html")), "UTF-8");
            out.println(homePage);
            //bodyBuilder = ResponseEntity.ok().body(homePage);
        }
        //return bodyBuilder;
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
