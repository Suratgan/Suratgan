package com.suratgan.backend.global.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventConfiguration {

    public EventConfiguration(ApplicationEventPublisher publisher) {
        Events.setPublisher(publisher);
    }

}
