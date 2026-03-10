package com.suratgan.backend.payment.application.service;

import java.util.UUID;

public interface CreatePayment {

    void create(UUID orderId, int amount);
}