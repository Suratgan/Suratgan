package com.suratgan.backend.global.infrastructure.security;

import com.suratgan.backend.global.domain.service.CustomerCheck;
import com.suratgan.backend.user.application.UserMeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerCheckImpl implements CustomerCheck {
    private final UserMeService userMeService;

    @Override
    public UUID getCustomerId() {
        return userMeService.getMe().getId();
    }

    @Override
    public String getCustomerName() {
        return userMeService.getMe().getNickname();
    }

    @Override
    public String getCustomerEmail() {
        return userMeService.getMe().getEmail();
    }

}
