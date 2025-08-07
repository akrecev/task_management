package ru.kretsev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Task Management System.
 */
@EnableCaching
@SpringBootApplication
public class TaskManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApp.class, args);
    }
}
