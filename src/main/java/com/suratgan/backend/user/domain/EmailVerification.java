package com.suratgan.backend.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_verification")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public EmailVerification(String email, String code, LocalDateTime expiredAt) {
        this.email = email;
        this.code = code;
        this.verified = false;
        this.expiredAt = expiredAt;
    }

    public static EmailVerification create(String email, String code, int expireMinutes) {
        return new EmailVerification(email, code, LocalDateTime.now().plusMinutes(expireMinutes));
    }

    public void verify(String inputCode) {
        if (LocalDateTime.now().isAfter(expiredAt)) {
            throw new IllegalStateException("인증 시간이 만료되었습니다.");
        }
        if(!this.code.equals(inputCode)) {
            throw new IllegalStateException("인증 코드가 일치하지 않습니다.");
        }
        this.verified = true;
    }

    public void refreshCode(String newCode, int expireMinutes) {
        this.code = newCode;
        this.verified = false;
        this.expiredAt = LocalDateTime.now().plusMinutes(expireMinutes);
    }
}
