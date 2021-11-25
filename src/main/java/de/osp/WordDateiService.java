package de.osp;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.StyledEditorKit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

 public class WordDateiService {
     @Autowired
     StudentRepository studentRepository;

     public void erstelleAnmeldungAlsWordDokument(String name, String birthDate, String city, String emailAddress, String emergencyNumber,String emergencyPerson,
                                                  String grade, String gradeTeacher, Boolean isLegalOfAge,
                                                  String number, String physicalImpairment, String specialNutrition, Boolean status, String street,
                                                  String surName){

         ersetzePlaceHolderInWordDokument("PLACEHOLDER_NAME", name);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_BIRTHDATE", birthDate);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_CITY", city);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMAIL", emailAddress);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMERGENCYNUMBER", emergencyNumber);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMERGENCYPERSON", emergencyPerson);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_GRADE", grade);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_GRADETEACHER", gradeTeacher);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_ISOFAGE", Boolean.toString(isLegalOfAge));
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_NUMBER", number);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_PHYSICALIMPAIRMENT", physicalImpairment);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_SPECIALNUTRITION", specialNutrition);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_STATUS", Boolean.toString(status));
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_STREET", street);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_SURNAME", surName);
     }

     private static void ersetzePlaceHolderInWordDokument(String artDesPlaceHolders, String wertAusDerDatenbank) {
         try {
             XWPFDocument doc = new XWPFDocument(OPCPackage.open("C:\\Projekte\\Schulprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular 2022.docx"));
             for (XWPFParagraph p : doc.getParagraphs()) {
                 List<XWPFRun> runs = p.getRuns();
                 if (runs != null) {
                     for (XWPFRun r : runs) {
                         String text = r.getText(0);
                         if (text != null && text.contains(artDesPlaceHolders)) {
                             text = text.replace(artDesPlaceHolders, wertAusDerDatenbank);
                             r.setText(text, 0);
                         }
                     }
                 }
             }
             for (XWPFTable tbl : doc.getTables()) {
                 for (XWPFTableRow row : tbl.getRows()) {
                     for (XWPFTableCell cell : row.getTableCells()) {
                         for (XWPFParagraph p : cell.getParagraphs()) {
                             for (XWPFRun r : p.getRuns()) {
                                 String text = r.getText(0);
                                 if (text != null && text.contains(artDesPlaceHolders)) {
                                     text = text.replace(artDesPlaceHolders, wertAusDerDatenbank);
                                     r.setText(text,0);
                                 }
                             }
                         }
                     }
                 }
             }
             doc.write(new FileOutputStream("Anmeldeformular_Ausgefuellt.docx"));
         } catch (IOException e) {
             e.printStackTrace();
         } catch (InvalidFormatException e) {
             e.printStackTrace();
         }
     }
 }
