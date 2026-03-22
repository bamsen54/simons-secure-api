package com.simon.simonssecureapi.dto;

public record MemberDto(

        Long id,
        // Member
        String firstName,
        String lastName,
        String email,
        String phone,
        String dateOfBirth,

        // Address
        String street,
        String postalCode,
        String city
) {
}
