package com.simon.simonssecureapi.dto;

import com.simon.simonssecureapi.Role;
import jakarta.validation.constraints.*;

public record AppUserRegistrationDto(

        @NotBlank @Size(max = 64) String username,
        @NotBlank @Size(max = 64) String password,
        @NotNull Role role,

        @NotBlank @Size(max=64) String firstName,
        @NotBlank @Size(max=64)  String lastName,
        @NotBlank @Size(max = 128) String email,
        @Size @Size(max = 24)  @Pattern(regexp = "^\\+?[0-9\\s-()]+$")String phone,
        @NotBlank @Size(max = 20) String dateOfBirth,

        String street,
        String postalCode,
        String city
) {}