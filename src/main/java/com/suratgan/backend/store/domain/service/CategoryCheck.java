package com.suratgan.backend.store.domain.service;

import java.util.List;
import java.util.UUID;

public interface CategoryCheck {
    boolean exists(List<UUID> categoryIds);
}
