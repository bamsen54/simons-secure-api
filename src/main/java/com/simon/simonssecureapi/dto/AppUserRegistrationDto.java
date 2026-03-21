package com.simon.simonssecureapi.dto;

import com.simon.simonssecureapi.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AppUserRegistrationDto(

        // AppUser
        @NotNull @NotEmpty String username,
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
) {}