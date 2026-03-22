package com.simon.simonssecureapi.service;

import com.simon.simonssecureapi.dto.MemberDto;
import com.simon.simonssecureapi.dto.MemberPutDto;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;
import com.simon.simonssecureapi.exception.ResourceAlreadyExistsException;
import com.simon.simonssecureapi.mapper.MemberMapper;
import com.simon.simonssecureapi.repository.AdminRepo;
import com.simon.simonssecureapi.repository.MemberRepo;
import com.simon.simonssecureapi.util.ApiUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final AdminRepo adminRepo;
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;

    public MemberService(AdminRepo adminRepo, MemberRepo memberRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo       = adminRepo;
        this.memberRepo      = memberRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MemberDto> getAllMembers() {

        List<AppUser> appUsersWithMember = adminRepo.findAllByMemberIsNotNull();

        return appUsersWithMember.stream().map(appUser -> MemberMapper.toDto(appUser.getId(), appUser.getMember())).toList();
    }

    @Transactional
    public MemberDto putOwnMember(Long id, MemberPutDto dto) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        AppUser appUser = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No member with that username"));

        if(memberRepo.existsByDateOfBirthAndIdNot(dto.dateOfBirth(), appUser.getMember().getId()))
            throw new ResourceAlreadyExistsException("A member with that dob already exists");

        if (appUser.getMember() == null) {
            throw new AccessDeniedException("User has no member profile");
        }
        if (!appUser.getId().equals(id)) {
            throw new AccessDeniedException("You can only update your own profile");
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

        return MemberMapper.toDto(id, member);
    }
}
