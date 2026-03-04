package com.suratgan.backend.store.infrastructure;

import com.suratgan.backend.store.domain.service.MenuDescriptionGenerate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuDescriptionGenerateImpl implements MenuDescriptionGenerate {
    @Override
    public String generate(String menuName, List<String> ingredients) {
        return "";
    }
}
