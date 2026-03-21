package com.simon.simonssecureapi.config;

import com.simon.simonssecureapi.Role;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;
import com.simon.simonssecureapi.repository.AdminRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AdminRepo adminRepo) {

        return args -> {

            if(adminRepo.count() == 0) {

                Address address1 = new Address("Javavagen 1", "11111", "Jakarta");
                Member member1   = new Member("simon", "toivola", address1, "simon@mail.com", "0702915123", "1994-11-28");
                AppUser admin1   = new AppUser("simon_admin", "1234", Role.ADMIN,  member1);

                Member member2 = new Member("sara", "nilsson", address1, "sara@mail.com", "-", "1998-01-01");
                AppUser admin2 = new AppUser("sara_admin", "1234", Role.ADMIN,  member2);

//                Address address2 = new Address("Malmövägen 1", "91919", "Malmö");
//                Member member3   = new Member("Dennis", "Wiklund", address2, "dennis@mail.com", "-", "1994-08-01");
//                AppUser admin3 = new AppUser("dennis_admin", "1234", Role.ADMIN,  member3);

                AppUser admin3 = new AppUser("admin", "1234", Role.ADMIN);

                adminRepo.saveAll(List.of(admin1, admin2, admin3));
            }
        };
    }
}

