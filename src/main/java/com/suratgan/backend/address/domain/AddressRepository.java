package com.suratgan.backend.address.domain;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    boolean existsByUserId(UUID userId);
    Optional<Address> findByUserId(UUID userId);
    Address save(Address address);
    void delete(Address address);
}
