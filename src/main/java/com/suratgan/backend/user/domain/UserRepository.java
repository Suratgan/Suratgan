package com.suratgan.backend.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByNickname(String nickname);

    Optional<User> findById(UUID id);
}
