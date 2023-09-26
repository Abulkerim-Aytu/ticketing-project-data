package com.cydeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.ModelMap;

@SpringBootApplication // this includes @Configuration
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    // WHEN WE USE THIRD PARTY LIBRARY YOU NEED TO CREATE THE @BEAN
    // I am trying to add bean in the container throw @Bean annotation
    // Create a class annotated with @Configuration
    // Write a method which return the object that you trying to add in the container
    // Annotate this method with @Bean






}
