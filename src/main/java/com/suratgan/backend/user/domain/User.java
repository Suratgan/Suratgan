package com.suratgan.backend.user.domain;

import com.suratgan.backend.global.domain.BaseEntity;
import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "p_users")
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

    @Column(name = "phone")
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isDeleted;

    public static User create(String nickname, String password, String email, Role role, String phoneNumber) {
        if (role != Role.CUSTOMER && role != Role.OWNER) {
            throw new IllegalArgumentException("가입 가능한 role은 CUSTOMER 또는 OWNER입니다.");
        }
        return User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .role(role)
                .phoneNumber(phoneNumber)
                .isDeleted(false)
                .build();
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeNickname(String newNickname) {
        if (this.isDeleted) throw new IllegalStateException("탈퇴한 사용자입니다.");
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 비어있을 수 없습니다.");
        }
        this.nickname = newNickname;
    }

    public void softDelete() {
        if (this.isDeleted) return;
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void changePassword(String encodedNewPassword) {
        if (this.isDeleted) {
            throw new BusinessException(ErrorCode.USER_DELETED);
        }
        if (encodedNewPassword == null || encodedNewPassword.isBlank()) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_NULL);
        }
        this.password = encodedNewPassword;
    }
}
