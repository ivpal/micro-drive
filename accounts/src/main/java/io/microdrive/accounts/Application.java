package io.microdrive.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.microdrive")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
