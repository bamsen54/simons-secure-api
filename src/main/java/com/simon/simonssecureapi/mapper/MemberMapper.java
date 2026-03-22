package com.simon.simonssecureapi.mapper;

import com.simon.simonssecureapi.dto.AppUserDto;
import com.simon.simonssecureapi.dto.MemberDto;
import com.simon.simonssecureapi.entity.AppUser;
import com.simon.simonssecureapi.entity.Member;

public class MemberMapper {

    public static MemberDto toDto(Long appUserId, Member member) {

        if ( member == null) return null;

        return new MemberDto(
            appUserId,
            member.getFirstName(),
            member.getLastName(),
            member.getEmail(),
            member.getPhone(),
            member.getDateOfBirth(),

            member.getAddress().getStreet(),
            member.getAddress().getPostalCode(),
            member.getAddress().getCity()
        );
    }

}
