package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "ru.practicum*")
@SpringBootApplication
public class EWMMainServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EWMMainServiceApp.class, args);
    }
}
