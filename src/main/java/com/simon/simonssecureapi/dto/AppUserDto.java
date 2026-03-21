package com.simon.simonssecureapi.dto;

import com.simon.simonssecureapi.Role;

public record AppUserDto(
        // AppUser
        String username,
        String password,
        Role role,

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
