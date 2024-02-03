package com.myblog8;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Myblog8Application {

    public static void main(String[] args) {
        SpringApplication.run(Myblog8Application.class, args);
    }

    @Bean   // Use When third party library added @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
