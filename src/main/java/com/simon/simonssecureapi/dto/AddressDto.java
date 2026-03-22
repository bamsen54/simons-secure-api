package com.simon.simonssecureapi.dto;

import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @NotNull Long id,
        String street,
        String postalCode,
        String city) {
}
