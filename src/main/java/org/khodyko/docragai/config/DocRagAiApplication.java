package org.khodyko.docragai.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "org.khodyko.docragai", exclude = {DataSourceAutoConfiguration.class})
@RequiredArgsConstructor
@Slf4j
public class DocRagAiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DocRagAiApplication.class, args);
    }


    @Override
    public void run(String... args) {
    }
}
