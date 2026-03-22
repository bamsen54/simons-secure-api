package com.simon.simonssecureapi.service;

import com.simon.simonssecureapi.dto.MemberDto;
import com.simon.simonssecureapi.dto.MemberPutDto;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;
import com.simon.simonssecureapi.mapper.MemberMapper;
import com.simon.simonssecureapi.repository.AdminRepo;
import com.simon.simonssecureapi.util.ApiUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    public MemberService(AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberDto> getAllMembers() {

        List<AppUser> appUsersWithMember = adminRepo.findAllByMemberIsNotNull();

        return appUsersWithMember.stream().map(appUser -> MemberMapper.toDto(appUser.getId(), appUser.getMember())).toList();
    }

    public MemberDto putOwnMember(Long id, MemberPutDto dto) {
        // 1. Hämta inloggad användares username
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        // 2. Hitta AppUser (via adminRepo)
        AppUser appUser = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Användare saknas"));

        // 3. Kolla att AppUser har en kopplad member
        if (appUser.getMember() == null) {
            throw new AccessDeniedException("Användaren har ingen medlemsprofil");
        }


        // 4. Kolla att id matchar den kopplade membern
        if (!appUser.getId().equals(id)) {
            throw new AccessDeniedException("Du kan bara uppdatera din egen profil");
        }

        // 5. Hämta membern (via appUser)
        Member member = appUser.getMember();

        if (dto.firstName() != null) {
            member.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            member.setLastName(dto.lastName());
        }
        if (dto.email() != null) {
            member.setEmail(dto.email());
        }
        if (dto.phone() != null) {
            member.setPhone(dto.phone());
        }
        if (dto.dateOfBirth() != null) {
            member.setDateOfBirth(dto.dateOfBirth());
        }

        if(dto.password() != null) {
            appUser.setPassword(passwordEncoder.encode(dto.password()));
        }

        Address updatedAddress  = ApiUtil.toCompleteAddress(dto.street(), dto.postalCode(), dto.city());
        Address existingAddress =  ApiUtil.getAddressWithFields(updatedAddress);

        if(existingAddress != null)
            appUser.getMember().setAddress(existingAddress);

        else
            appUser.getMember().setAddress(updatedAddress);

        adminRepo.save(appUser);

        // 9. Returnera MemberDto
        return MemberMapper.toDto(id, member);
    }
}
