package kr.megaptera.backendsurvivalweek10.dtos;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
