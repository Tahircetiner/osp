package de.osp;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.util.List;

 public class WordDateiService {
     @Autowired
     StudentRepository studentRepository;

     public void erstelleAnmeldungAlsWordDokument(String name, String birthDate, String city, String emailAddress, String emergencyNumber,String emergencyPerson,
                                                  String grade, String gradeTeacher, Boolean isLegalOfAge,
                                                  String number, String physicalImpairment, String specialNutrition, Boolean status, String street,
                                                  String surName) throws IOException, InvalidFormatException {

         XWPFDocument doc = new XWPFDocument(OPCPackage.open("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular2022.docx"));
         doc.write(new FileOutputStream("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\copies\\Anmeldeformular2022.docx"));
         doc.close();
         ersetzePlaceHolderInWordDokument("NAME", name);
         ersetzePlaceHolderInWordDokument("BIRTHDATE", birthDate);
         ersetzePlaceHolderInWordDokument("CITY", city);
         ersetzePlaceHolderInWordDokument("EMAIL", emailAddress);
         ersetzePlaceHolderInWordDokument("EMERGENCYNUMBER", emergencyNumber);
         ersetzePlaceHolderInWordDokument("EMERGENCYPERSON", emergencyPerson);
         ersetzePlaceHolderInWordDokument("GRADE", grade);
         ersetzePlaceHolderInWordDokument("TEACHER", gradeTeacher);
         ersetzePlaceHolderInWordDokument("ISOFAGE", Boolean.toString(isLegalOfAge));
         ersetzePlaceHolderInWordDokument("NUMBER", number);
         ersetzePlaceHolderInWordDokument("PHYSICALIMPAIRMENT", physicalImpairment);
         ersetzePlaceHolderInWordDokument("SPECIALNUTRITION", specialNutrition);
         ersetzePlaceHolderInWordDokument("STATUS", Boolean.toString(status));
         ersetzePlaceHolderInWordDokument("STREET", street);
         ersetzePlaceHolderInWordDokument("SURNAME", surName);
         File file = new File("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular2022.docx");
         file.delete();

         XWPFDocument doc2 = new XWPFDocument(OPCPackage.open("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\copies\\Anmeldeformular2022.docx"));
         doc2.write(new FileOutputStream("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular2022.docx"));
         doc2.close();

         File file1 = new File("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\copies\\Anmeldeformular2022.docx");
         file1.delete();
     }

     private static void ersetzePlaceHolderInWordDokument(String artDesPlaceHolders, String wertAusDerDatenbank) throws InvalidFormatException, IOException {
         try {
             XWPFDocument doc = new XWPFDocument(OPCPackage.open("C:\\neustaprojekte\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular2022.docx"));
             for (XWPFParagraph p : doc.getParagraphs()) {
                 List<XWPFRun> runs = p.getRuns();
                 if (runs != null) {
                     for (XWPFRun r : runs) {
                         String text = r.getText(0);
                         System.out.println("text " + text);
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
                                 System.out.println("text tables " + text);
                                 if (text != null && text.contains(artDesPlaceHolders)) {
                                     text = text.replace(artDesPlaceHolders, wertAusDerDatenbank);
                                     r.setText(text,0);
                                 }
                             }
                         }
                     }
                 }
             }
             doc.write(new FileOutputStream("Anmeldeformular2022ausgefuellt.docx"));
             doc.close();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (InvalidFormatException e) {
             e.printStackTrace();
         }
     }
 }
