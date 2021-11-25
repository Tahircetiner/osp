package de.osp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class TestController {

    @RequestMapping("/testing")
    @ResponseBody
    public ResponseEntity welcome() throws IOException {
        String s = new String(Files.readAllBytes(Paths.get("C://Projekte//Schulprojekte//osp//src//main//resources//static//intern//overview." + "html")), "UTF-8");
        return new ResponseEntity<String>(s, HttpStatus.OK);
    }
}
