package com.suratgan.backend.address.presentation;

import com.suratgan.backend.address.application.UserAddressService;
import com.suratgan.backend.address.application.dto.AddressResponseDto;
import com.suratgan.backend.address.application.dto.CreateAddressRequestDto;
import com.suratgan.backend.address.application.dto.UpdateAddressRequestDto;
import com.suratgan.backend.address.domain.Address;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me/address")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @GetMapping
    public ResponseEntity<AddressResponseDto> getMyAddress() {
        return ResponseEntity.ok(userAddressService.getMyAddress());
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(
            @Valid @RequestBody CreateAddressRequestDto request
    ) {
        return ResponseEntity.ok(userAddressService.createAddress(request));
    }

    @PatchMapping
    public ResponseEntity<AddressResponseDto> updateAddress(
            @Valid @RequestBody UpdateAddressRequestDto request
    ) {
        return ResponseEntity.ok(userAddressService.updateAddress(request));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAddress() {
        return ResponseEntity.ok(userAddressService.deleteAddress());
    }
}
