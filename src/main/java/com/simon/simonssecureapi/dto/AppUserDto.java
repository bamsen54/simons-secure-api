package com.simon.simonssecureapi.dto;

import com.simon.simonssecureapi.entity.Role;

public record AppUserDto(
        Long id,
        String username,
        Role role,

        String firstName,
        String lastName,
        String email,
        String phone,
        String dateOfBirth,

        String street,
        String postalCode,
        String city
) {
}
