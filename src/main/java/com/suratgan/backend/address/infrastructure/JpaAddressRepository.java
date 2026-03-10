package com.suratgan.backend.address.infrastructure;

import com.suratgan.backend.address.domain.Address;
import com.suratgan.backend.address.domain.AddressRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<Address, UUID>, AddressRepository {
    boolean existsByUserId(UUID userId);
    Optional<Address> findByUserId(UUID userId);
}
