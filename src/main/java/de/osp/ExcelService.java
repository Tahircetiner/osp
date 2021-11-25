package de.osp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;


public class ExcelService {

    @Autowired
    StudentRepository studentRepository;
/*
    private void  writeIntoExcel(){
        Workbook workbook = new XSSFWorkbook();
        Iterable<Student> schuelerDaten =  studentRepository.findAll();

        Sheet sheet = workbook.createSheet("Schueler");
        String[] spaltenUeberschriften = {"Idenktifikationsnummer", "Geburtsdatumm", "Stadt", "Emal-Adresse","Not-Rufnummer", "Person fuer Notffälle", "Klasse", "Klassenlehrer", "Name", "Nummer", "Physische Beeinträchtigungen", "Spezielle Ernährung", "Status der Anmeldung", "Straße", "Nachname"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i<spaltenUeberschriften.length; i++){
            Cell zelle = headerRow.createCell(i);
            zelle.setCellValue(spaltenUeberschriften[i]);
        }
        CreationHelper creationHelper = workbook.getCreationHelper();
        int zeilennummer = 1;
        for (Student i: schuelerDaten) {
            Row zeile = sheet.createRow(zeilennummer++);
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

 */

    /*
    //Schreibt eine Arbeitsmappe in eine Exceldatei
    private void createExcelFile(File file) throws FileNotFoundException, IOException {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {

            workbook.write(fileOut);

        } catch (FileNotFoundException fnfe) {
            throw new IOException("Ausnahmefehler: Die Datei " + file.getName() + " konnte nicht erzeugt werden!");
        } catch (IOException ioe) {
            throw new IOException("Ausnahmefehler: Die Datei " + file.getName() + " konnte nicht erzeugt werden!");
        }

    }

    */



}
