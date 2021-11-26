package de.osp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {
    //DateiAusgabePfad, was kommt hier rein???
    final private String DATEI_AUSGABE_PFAD                  = "C:\\Projekte\\Schulprojekte\\osp\\src\\main\\resources\\static\\excel\\test.xlsx";

    //Hier sind die Strings fuer die SpaltenÜberschriften der ExcelTabelle
    final private String ID                  = "Idenktifikationsnummer";
    final private String AGE                 = "Geburtsdatumm";
    final private String CITY                = "Stadt";
    final private String EMAIL_ADRESS        = "Emal-Adresse";
    final private String EMERGENCY_NUMBER    = "Not-Rufnummer";
    final private String EMERGENCY_PERSON    = "Person fuer Notffälle";
    final private String GRADE               = "Klasse";
    final private String GRADE_TEACHER       = "Klassenlehrer";
    final private String IS_OF_LEGAL_AGE     = "ist Volljährig";
    final private String NAME                = "Name";
    final private String NUMBER              = "Nummer";
    final private String PHYSICAL_IMPAIRMENT = "Physische Beeinträchtigungen";
    final private String SPECIAL_NUTRITION   = "Spezielle Ernährung";
    final private String STATUS              = "Status der Anmeldung";
    final private String STREET              = "Straße";
    final private String SUR_NAME            = "Nachname";

    @Autowired
    StudentRepository studentRepository;

    public void  writeIntoExcel(){
        Workbook excelArbeitsmappe          = new XSSFWorkbook();
        Iterable<Student> schuelerDaten     =  studentRepository.findAll();
        Sheet einzelnesSheetInExcelMappe    = excelArbeitsmappe.createSheet("Schueler");
        String[] spaltenUeberschriften      = {ID, AGE, CITY, EMAIL_ADRESS, EMERGENCY_NUMBER, EMERGENCY_PERSON, GRADE, GRADE_TEACHER, IS_OF_LEGAL_AGE, NAME, NUMBER, PHYSICAL_IMPAIRMENT, SPECIAL_NUTRITION, STATUS, STREET, SUR_NAME};
        Row headerZeile                     = einzelnesSheetInExcelMappe.createRow(0);

        fuellerErsteZeileMitUeberschriften(spaltenUeberschriften, headerZeile);
        geheDurchJederZeileUndFuelleDatenInExcelSheet(schuelerDaten, einzelnesSheetInExcelMappe);
        gibExcelDateiAusUndSchliesseObjekte(excelArbeitsmappe);
    }

    private void gibExcelDateiAusUndSchliesseObjekte(Workbook excelArbeitsmappe) {
        try {
            FileOutputStream fileOut = new FileOutputStream(DATEI_AUSGABE_PFAD);
            excelArbeitsmappe.write(fileOut);

            fileOut.close();
            excelArbeitsmappe.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void geheDurchJederZeileUndFuelleDatenInExcelSheet(Iterable<Student> schuelerDaten, Sheet einzelnesSheetInExcelMappe) {
        int zeilennummer = 1;
        for (Student i: schuelerDaten) {
            Row zeile = einzelnesSheetInExcelMappe.createRow(zeilennummer++);
            zeile.createCell(0).setCellValue(i.getId());
            zeile.createCell(0).setCellValue(i.getAge());
            zeile.createCell(0).setCellValue(i.getCity());
            zeile.createCell(0).setCellValue(i.getEmailAddress());
            zeile.createCell(0).setCellValue(i.getEmergencyNumber());
            zeile.createCell(0).setCellValue(i.getEmergencyPerson());
            zeile.createCell(0).setCellValue(i.getGrade());
            zeile.createCell(0).setCellValue(i.getGradeTeacher());
            zeile.createCell(0).setCellValue(i.getName());
            zeile.createCell(0).setCellValue(i.getNumber());
            zeile.createCell(0).setCellValue(i.getPhysicalImpairment());
            zeile.createCell(0).setCellValue(i.getSpecialNutrition());
            zeile.createCell(0).setCellValue(i.getStatus());
            zeile.createCell(0).setCellValue(i.getStreet());
            zeile.createCell(0).setCellValue(i.getSurName());
        }
    }


    private void fuellerErsteZeileMitUeberschriften(String[] spaltenUeberschriften, Row headerZeile) {
        for (int i = 0; i< spaltenUeberschriften.length; i++){
            Cell zelle = headerZeile.createCell(i);
            zelle.setCellValue(spaltenUeberschriften[i]);
        }
    }

    //CreationHelper wird hier nicht gebraucht, ist also toter Code eigentlich. Würd ich aber drin lassen.
    //Das kann dafür verwendet werden Die einzelenn Zellen zu Formatieren mit fareb oder schriftart
    //
    //CreationHelper creationHelper = excelArbeitsmappe.getCreationHelper();
}
