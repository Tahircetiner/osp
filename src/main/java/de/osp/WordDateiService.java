package de.osp;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

 public class WordDateiService {
     @Autowired
     StudentRepository studentRepository;

     public void erstelleAnmeldungAlsWordDokument(){

         ersetzePlaceHolderInWordDokument("PLACEHOLDER_NAME", wertAusDatenbank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_BIRTHDATE", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_CITY", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMAIL", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMERGENCYNUMBER", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_EMERGENCYPERSON", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_GRADE", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_GRADETEACHER", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_ISOFAGE", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_NUMBER", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_PHYSICALIMPAIRMENT", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_SPECIALNUTRITION", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_STATUS", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_STREET", wertAusDerDatenBank);
         ersetzePlaceHolderInWordDokument("PLACEHOLDER_SURNAME", wertAusDerDatenBank);
     }

     private static void ersetzePlaceHolderInWordDokument(String artDesPlaceHolders) {
         try {
             XWPFDocument doc = new XWPFDocument(OPCPackage.open("C:\\Users\\Patrick\\Patricks Dateien\\Schule GSO\\Oberstufenprojekt\\Projekt_git\\osp\\src\\main\\resources\\static\\intern\\docs\\Anmeldeformular 2022.docx"));
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
