package de.osp;
import de.osp.Service.Hasher;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("/generateSession")
    public ValidationMessage setSession(HttpServletRequest httpServletRequest ,HttpSession httpSession){
        httpSession = httpServletRequest.getSession(false);
        return new ValidationMessage();
    }

    @PostMapping("/login")
    public ValidationMessage authenticate(@RequestBody Teacher teacher, HttpSession httpSession, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        Teacher teacherDb = teacherRepository.findByUsername(teacher.getUsername());
        ValidationMessage validationMessage = new ValidationMessage();

        httpSession = httpServletRequest.getSession(false);
        String pwHash = "";
        try {
            pwHash = Hasher.hash(teacher.getPassword());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(teacherDb != null){
            if(!pwHash.equals(teacherDb.getPassword())) {
                validationMessage.setMessage("Username oder Passwort ist falsch");
            }
            else{
                validationMessage.setMessage("Username und Passwort ist richtig.");
            }
        }
        httpSession.setAttribute("username", teacher.getUsername());
        httpSession.setAttribute("hashedpassword", pwHash);
        return validationMessage;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/intern/overview")
    @ResponseBody
    public ModelAndView getOverviewPage(HttpSession httpSession, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, IOException, InterruptedException {
        Thread.sleep(1500);
        Boolean isGlobalSessionNull = Objects.isNull(httpSession);
        Object user = null;
        Object pw = null;
        ModelAndView modelAndView = new ModelAndView();
        if(!isGlobalSessionNull){
            user = httpSession.getAttribute("username");
            pw = httpSession.getAttribute("hashedpassword");
        }
        if(user != null && pw != null){
            Teacher teacherDb = teacherRepository.findByUsername(user.toString());
            if(!(Hasher.hash(teacherDb.getPassword()).equals(pw))) {
                modelAndView.setViewName("overview");
                return modelAndView;
            }
        }
        else{
            modelAndView.setViewName("index");
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping("/adminDataAll")
    public Iterable<Student> getAllStudents(){
        return studentRepository.findAll();
    }



    @PostMapping("/formular")
    public ValidationMessage saveStudentInformation(@RequestBody Student student){
        Validation validation = new Validation();
        ValidationMessage validationMessage = new ValidationMessage();

        List<Student> studentList = studentRepository.findAllBySurNameAndNameAndNumberAndCityAndStreetAndAgeAndEmailAddressAndGradeAndGradeTeacherAndSpecialNutritionAndPhysicalImpairmentAndIsOfLegalAgeAndEmergencyNumberAndEmergencyPerson(student.getSurName(), student.getName(), student.getNumber(), student.getCity(), student.getStreet(), student.getAge(), student.getEmailAddress(), student.getGrade(), student.getGradeTeacher(), student.getSpecialNutrition(), student.getPhysicalImpairment(), student.getIsOfLegalAge(), student.getEmergencyNumber(), student.getEmergencyPerson());
        if(Boolean.FALSE.equals(validation.hasAllFieldsFilled(student)) || !studentList.isEmpty()){
            validationMessage.setMessage("Sie haben entweder nicht alle Felder ausgefüllt oder Sie haben sich bereits registriert.");
        }
        else{
            validationMessage.setMessage("Sie haben sich erfolgreich an dem Austauschprogramm angemeldet. Bitte wenden Sie sich an ihren zuständigen Lehrer mit ihrem ausgefüllten Anmeldeformular.");
            studentRepository.save(student);
        }
        return validationMessage;
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(String param) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("content-disposition", "attachment; filename=" + "anmeldeformular.docx");
        Resource resource1 = new ClassPathResource("/static/intern/docs/Anmeldeformular2022.docx");

        System.out.println(resource1.getURL().getPath().replaceAll("%20", "/"));
        System.out.println(resource1.getURL());
        System.out.println(resource1.getURI());
        String tmp = resource1.getURL().getPath();
        String tmp2 = tmp.replaceAll("/", "\\\\");
        System.out.println(tmp);
        File file = new File(tmp2);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
