package de.osp;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

 public class WordDateiService {


     public static void erstelleAnmeldungAlsWordDokument(){

        try {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open("C://Users//Patrick//Patricks Dateien//Schule GSO//Oberstufenprojekt//Projekt_git//osp//src//main//resources//static//intern//docs//Anmeldeformular.doc"));
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("PLACEHOLDER_NAME")) {
                            text = text.replace("PLACEHOLDER_NAME", "teesteeee");
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
                                if (text != null && text.contains("needle")) {
                                    text = text.replace("needle", "haystack");
                                    r.setText(text,0);
                                }
                            }
                        }
                    }
                }
            }
            doc.write(new FileOutputStream("output.docx"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
