package com.suratgan.backend.address.application;

import com.suratgan.backend.address.application.dto.AddressResponseDto;
import com.suratgan.backend.address.application.dto.CreateAddressRequestDto;
import com.suratgan.backend.address.application.dto.UpdateAddressRequestDto;
import com.suratgan.backend.address.domain.Address;
import com.suratgan.backend.address.domain.AddressRepository;
import com.suratgan.backend.global.domain.service.AddressToCoords;
import com.suratgan.backend.global.exception.BusinessException;
import com.suratgan.backend.global.exception.ErrorCode;
import com.suratgan.backend.global.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAddressService {

    private final AddressRepository addressRepository;
    private final AddressToCoords addressToCoords;

    @Transactional(readOnly = true)
    public AddressResponseDto getMyAddress() {
        UUID userId = SecurityUtils.currentUserId();

        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        return toResponse(address);
    }

    @Transactional
    public AddressResponseDto createAddress(CreateAddressRequestDto request) {
        UUID userId = SecurityUtils.currentUserId();

        if (addressRepository.existsByUserId(userId)) {
            throw new BusinessException(ErrorCode.ADDRESS_ALREADY_EXISTS);
        }

        double[] coords = addressToCoords.convert(request.getAddress());
        log.info("위도는 {}, 경도는 {}", coords[0], coords[1]);
        if(coords == null) {
            throw new BusinessException(ErrorCode.INVALID_ADDRESS);
        }

        Address address = Address.create(
                userId,
                request.getAddress(),
                request.getDetailAddress(),
                coords[0],
                coords[1]
        );

        addressRepository.save(address);

        return toResponse(address);
    }

    @Transactional
    public AddressResponseDto updateAddress(UpdateAddressRequestDto request) {
        UUID userId = SecurityUtils.currentUserId();

        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        double[] coords = addressToCoords.convert(request.getAddress());
        log.info("경도는 {}, 위도는 {}", coords[0], coords[1]);
        if(coords == null) {
            throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        address.update(
                request.getAddress(),
                request.getDetailAddress(),
                coords[0],
                coords[1]
        );

        addressRepository.save(address);

        return toResponse(address);
    }

    @Transactional
    public String deleteAddress() {
        UUID userId = SecurityUtils.currentUserId();

        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        addressRepository.delete(address);
        return "배달 주소가 삭제되었습니다.";
    }

    private AddressResponseDto toResponse(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .longitude(address.getLongitude())
                .latitude(address.getLatitude())
                .build();
    }
}
