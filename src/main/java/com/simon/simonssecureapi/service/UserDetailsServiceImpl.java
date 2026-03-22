package com.simon.simonssecureapi.service;



import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.repository.AdminRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepo adminRepo;

    public UserDetailsServiceImpl(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Användare saknas: " + username));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name());

        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                List.of(authority)
        );
    }
}