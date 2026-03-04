package com.suratgan.backend.user.infrastructure;

import com.suratgan.backend.user.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailOrderByIdDesc(String email);

    Optional<EmailVerification> findByEmailAndVerifiedTrue(String email);

    boolean existsByEmailAndVerifiedTrue(String email);

    void deleteByEmail(String email);
}
