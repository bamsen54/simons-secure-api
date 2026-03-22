package com.simon.simonssecureapi.dto;

import com.simon.simonssecureapi.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberPutDto(

        @NotBlank @Size(max=64) String firstName,
        @NotBlank @Size(max=64)  String lastName,
        @NotBlank @Size(max = 128) String email,
        @Size @Size(max = 24)  @Pattern(regexp = "^\\+?[0-9\\s-()]+$")String phone,
        @NotBlank @Size(max = 20) String dateOfBirth,

        // Address
        String street,
        String postalCode,
        String city,

        @NotBlank String password
) {
}
