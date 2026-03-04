package com.suratgan.backend.user.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isDeleted;

    public static User create(String nickname, String password, String email, Role role) {
        if (role != Role.CUSTOMER && role != Role.OWNER) {
            throw new IllegalArgumentException("가입 가능한 role은 CUSTOMER 또는 OWNER입니다.");
        }
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.password = password;
        user.role = Role.CUSTOMER;
        user.isDeleted = false;
        return user;
    }
}
