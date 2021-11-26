package de.osp;
import de.osp.Service.Hasher;
import de.osp.Service.LoginCheck;
import org.apache.coyote.Request;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

    @Autowired
    ExcelService excelService;

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
        if(teacher == null){
            validationMessage.setMessage("Username oder Passwort ist falsch");
        }
        httpSession.setAttribute("username", teacher.getUsername());
        httpSession.setAttribute("hashedpassword", pwHash);
        return validationMessage;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/intern/overview")
    @ResponseBody
    public ModelAndView getOverviewPage(HttpSession httpSession, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, IOException, InterruptedException {
        Thread.sleep(1500);
        ModelAndView modelAndView = new ModelAndView();
        if((new LoginCheck()).Check(httpSession, teacherRepository)) {
            modelAndView.setViewName("overview");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping("/adminDataAll")
    public Iterable<Student> getAllStudents(HttpSession httpSession) throws NoSuchAlgorithmException {
        if(!(new LoginCheck()).Check(httpSession, teacherRepository)) {
            return null;
        }
        return studentRepository.findAll();
    }



    @PostMapping("/formular")
    public ValidationMessage saveStudentInformation(@RequestBody Student student) throws IOException, InvalidFormatException {
        Validation validation = new Validation();
        ValidationMessage validationMessage = new ValidationMessage();

        List<Student> studentList = studentRepository.findAllBySurNameAndNameAndNumberAndCityAndStreetAndAgeAndEmailAddressAndGradeAndGradeTeacherAndSpecialNutritionAndPhysicalImpairmentAndIsOfLegalAgeAndEmergencyNumberAndEmergencyPerson(student.getSurName(), student.getName(), student.getNumber(), student.getCity(), student.getStreet(), student.getAge(), student.getEmailAddress(), student.getGrade(), student.getGradeTeacher(), student.getSpecialNutrition(), student.getPhysicalImpairment(), student.getIsOfLegalAge(), student.getEmergencyNumber(), student.getEmergencyPerson());
        if(Boolean.FALSE.equals(validation.hasAllFieldsFilled(student)) || !studentList.isEmpty()){
            validationMessage.setMessage("Sie haben entweder nicht alle Felder ausgefüllt oder Sie haben sich bereits registriert.");
        }
        else{
            validationMessage.setMessage("Sie haben sich erfolgreich an dem Austauschprogramm angemeldet. Bitte wenden Sie sich an ihren zuständigen Lehrer mit ihrem ausgefüllten Anmeldeformular.");
            studentRepository.save(student);

            WordDateiService wordDateiService = new WordDateiService();
            wordDateiService.erstelleAnmeldungAlsWordDokument(student.getName(),
                    student.getAge(), student.getCity(),student.getEmailAddress(), student.getEmergencyNumber(),
                    student.getEmergencyPerson(), student.getGrade(), student.getGradeTeacher(), student.getIsOfLegalAge(),
                    student.getNumber(),student.getPhysicalImpairment(), student.getSpecialNutrition()
                    , student.getStatus(), student.getStreet(), student.getSurName());
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

        File file = new File("C:\\Projekte\\Schulprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\generated\\Anmeldeformular2022.docx");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @RequestMapping(path = "/downloadExcelList", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadExcelList(String param) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("content-disposition", "attachment; filename=" + "ausgefuellt.xlsx");

        excelService.writeIntoExcel();

        File file = new File("C:\\Projekte\\Schulprojekte\\osp\\src\\main\\resources\\static\\excel\\test.xlsx");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
