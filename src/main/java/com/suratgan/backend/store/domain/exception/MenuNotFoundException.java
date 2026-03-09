package com.suratgan.backend.store.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class MenuNotFoundException extends HttpStatusCodeException {
    public MenuNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.");
    }
}
