package org.example.jwtfetch.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.jwtfetch.domain.entity.UserAccount;

public record SignUpForm(
        @NotBlank String username,
        @NotBlank String password) {

    public UserAccount toEntity() {
        return UserAccount.builder()
                .username(username)
                .password(password)
                .build();
    }
}
