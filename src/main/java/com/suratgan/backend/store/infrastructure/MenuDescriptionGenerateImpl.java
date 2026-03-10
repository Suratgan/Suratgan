package com.suratgan.backend.store.infrastructure;

import com.suratgan.backend.store.domain.service.MenuDescriptionGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuDescriptionGenerateImpl implements MenuDescriptionGenerate {
    private final ChatClient chatClient;

    @Override
    public String generate(String menuName, String menuInfo) {
        String prompt = String.format(
                "메뉴 이름: %s\n메뉴 정보: %s\n 해당 정보를 바탕으로 매력적인 메뉴 설명을 한 문장으로 작성해줘. 따옴표 없이 작성해줘.",
                menuName, menuInfo);

        return chatClient.prompt()
                .system("당신은 유명 맛집을 분석하는 전문적인 음식점 카피라이터입니다.")
                .user(prompt)
                .call()
                .content(); // 텍스트 내용 추출하여 반환
    }
}
