package org.example.jwtfetch.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "user_account")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAccount extends BaseEntity {
    private String username;
    @Setter
    private String password;
}
