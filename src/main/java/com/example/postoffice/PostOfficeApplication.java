package com.example.postoffice;

import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.PostOfficeRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PostOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostOfficeApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(PostOfficeRepository postOfficeRepository) {
//        return args -> {
//            PostOffice postOffice = PostOffice.builder()
//                    .index("100000")
//                    .address("AbraCadabra")
//                    .name("Rasscazovka")
//                    .build();
//            postOfficeRepository.save(postOffice);
//        };
//    }
}
