package de.osp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import static de.osp.WordDateiService.erstelleAnmeldungAlsWordDokument;

@EnableAsync
@SpringBootApplication
public class UserDatabaseApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserDatabaseApplication.class, args);
        erstelleAnmeldungAlsWordDokument();
    }

}
