package com.epam.company.util;

import com.epam.company.entity.EventDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(CustomSpringEvent<EventDepartment> event) {
        applicationEventPublisher.publishEvent(event);
    }
}