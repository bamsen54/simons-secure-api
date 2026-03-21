package com.simon.simonssecureapi.mapper;

import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.AppUserRegistrationDto;
import com.simon.simonssecureapi.entity.Address;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;

public class AppUserMapper {

    private AppUserMapper() {
    }

    public static AppUser fromRegistrationDto(AppUserRegistrationDto dto, String encodedPassword, Address address) {

        AppUser appUser = new AppUser();

        Member member = new Member();

        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setEmail(dto.email());
        member.setPhone(dto.phone());
        member.setDateOfBirth(dto.dateOfBirth());
        member.setAddress(address);

        appUser.setUsername(dto.username());
        appUser.setPassword(encodedPassword);
        appUser.setRole(dto.role());
        appUser.setMember(member);

        return appUser;
    }

    public static AppUser fromDto(AppUserDto dto) {

        AppUser appUser = new AppUser();

        Address address = new Address();

        address.setCity(dto.city());
        address.setStreet(dto.street());
        address.setPostalCode(dto.postalCode());

        Member member = new Member();

        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setEmail(dto.email());
        member.setPhone(dto.phone());
        member.setDateOfBirth(dto.dateOfBirth());
        member.setAddress(address);

        appUser.setUsername(dto.username());
        appUser.setPassword("");
        appUser.setRole(dto.role());
        appUser.setMember(member);

        return appUser;
    }

    public static AppUserDto toDto(AppUser user) {
        if (user == null) return null;

        return new AppUserDto(
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getMember().getFirstName(),
                user.getMember().getLastName(),
                user.getMember().getEmail(),
                user.getMember().getPhone(),
                user.getMember().getDateOfBirth(),
                user.getMember().getAddress().getStreet(),
                user.getMember().getAddress().getPostalCode(),
                user.getMember().getAddress().getCity()
        );
    }
}
