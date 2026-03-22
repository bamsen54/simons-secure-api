package com.simon.simonssecureapi.config;

import com.simon.simonssecureapi.entity.Role;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;
import com.simon.simonssecureapi.repository.AdminRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {

        return args -> {

            if(adminRepo.count() == 0) {

                String hashedPass = passwordEncoder.encode("1234");

                Address address1 = new Address("Java Street 1",   "11111", "Jakarta");
                Address address2 = new Address("Springgatan 42",  "12345", "Bootville");
                Address address3 = new Address("Hibernate Hill",  "54321", "ORM Town");
                Address address4 = new Address("RESTful Road",    "8080",  "API City");
                Address address5 = new Address("Git Gränd 7",     "94100", "Versionville");
                Address address6 = new Address("Mavenvägen 3",    "31800", "Buildheim");
                Address address7 = new Address("Postman Plats 1", "40000", "Teststaden");
                Address address8 = new Address("Docker Dock 5",   "2375",  "Container Bay");
                Address address9 = new Address("SQL Square 9",    "3306",  "Database City");
                Address address10 = new Address("JPA Junction",   "1337",  "Entity Town");

                AppUser appUser1 = new AppUser("a", hashedPass, Role.ADMIN, null);

                Member member1 = new Member("u", "u", address2, "u@mail.com", "-", "1970-01-01" );
                AppUser appUser2 = new AppUser("u", hashedPass, Role.MEMBER, member1);

                Member member2 = new Member("simon", "toivola", address1, "simontoivola@gmail.com", "0702915123", "1994-11-28");
                AppUser appUser3 = new AppUser("simon_admin", hashedPass, Role.ADMIN, member2);

                Member member3 = new Member("sara", "nilsson", address1, "simontoivola@gmail.com", "-", "1998-01-01");
                AppUser appUser4 = new AppUser("sara_admin", hashedPass, Role.ADMIN, member3);

                Member member4 = new Member("dennis", "wiklund", address3, "dennis@mail.com", "0701234567", "1994-08-01");
                AppUser appUser5 = new AppUser("dennis_member", hashedPass, Role.MEMBER, member4);

                Member member5 = new Member("lisa", "larsson", address4, "lisa@mail.com", "0702345678", "1992-03-15");
                AppUser appUser6 = new AppUser("lisa_member", hashedPass, Role.MEMBER, member5);

                Member member6 = new Member("erik", "johansson", address5, "erik@mail.com", "0703456789", "1988-07-22");
                AppUser appUser7 = new AppUser("erik_member", hashedPass, Role.MEMBER, member6);

                Member member7 = new Member("anna", "karlsson", address6, "anna@mail.com", "0704567890", "1991-11-03");
                AppUser appUser8 = new AppUser("anna_member", hashedPass, Role.MEMBER, member7);

                Member member8 = new Member("oscar", "nilsson", address7, "oscar@mail.com", "0705678901", "1995-09-19");
                AppUser appUser9 = new AppUser("oscar_member", hashedPass, Role.MEMBER, member8);

                Member member9 = new Member("linus", "berg", address8, "linus@mail.com", "0706789012", "1993-12-01");
                AppUser appUser10 = new AppUser("linus_member", hashedPass, Role.MEMBER, member9);

                Member member10 = new Member("maria", "holm", address9, "maria@mail.com", "0707890123", "1990-04-25");
                AppUser appUser11 = new AppUser("maria_member", hashedPass, Role.MEMBER, member10);

                Member member11 = new Member("johan", "ek", address10, "johan@mail.com", "0708901234", "1997-07-14");
                AppUser appUser12 = new AppUser("johan_member", hashedPass, Role.MEMBER, member11);

                adminRepo.saveAll(List.of(appUser1, appUser2, appUser3, appUser4, appUser5, appUser6,
                                          appUser7, appUser8, appUser9, appUser10, appUser11, appUser12));
            }
        };
    }
}

