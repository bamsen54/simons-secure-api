package com.simon.simonssecureapi.config;

import com.simon.simonssecureapi.Role;
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

                CharSequence hashedPass = passwordEncoder.encode("password");

                Address address1 = new Address("Javavagen 1", "11111", "Jakarta");
                Member member1   = new Member("simon", "toivola", address1, "simon@mail.com", "0702915123", "1994-11-28");
                AppUser admin1   = new AppUser("simon_admin", passwordEncoder.encode("1234"), Role.ADMIN,  member1);

                Member member2 = new Member("sara", "nilsson", address1, "sara@mail.com", "-", "1998-01-01");
                AppUser admin2 = new AppUser("sara_admin", passwordEncoder.encode("1234"), Role.ADMIN,  member2);


                AppUser appUserWhoIsNotMember = new AppUser("dennis_memeer",  passwordEncoder.encode("1234"), Role.MEMBER, null);

                Address address2 = new Address("Malmövägen", "99999", "Malmö");
                Member member3 = new Member("dennis", "wiklund", address2, "dennis.wiklund@mail.com", "-", "1994-08-01");
                AppUser normalUser1 = new AppUser("dennis_member", passwordEncoder.encode("1234"), Role.MEMBER, member3);

                adminRepo.saveAll(List.of(admin1, admin2, appUserWhoIsNotMember, normalUser1 ));
            }
        };
    }
}

