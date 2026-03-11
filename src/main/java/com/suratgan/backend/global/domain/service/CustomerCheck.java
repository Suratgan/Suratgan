package com.suratgan.backend.global.domain.service;

import java.util.UUID;

public interface CustomerCheck {
    UUID getCustomerId();
    String getCustomerName();
    String getCustomerEmail();
}
