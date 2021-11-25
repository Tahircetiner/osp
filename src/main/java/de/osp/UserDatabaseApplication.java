package de.osp;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@EnableAsync
@SpringBootApplication
public class UserDatabaseApplication {
    public static void main(String[] args) throws InvalidFormatException, IOException {
        SpringApplication.run(UserDatabaseApplication.class, args);
        XWPFDocument document = new XWPFDocument(OPCPackage.open("template.docx"));
    }
}
