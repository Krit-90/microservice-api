package com.epam.company.util;

import com.epam.company.entity.EventDepartment;
import com.epam.company.repository.EventDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventListener {
    private final EventDepartmentRepository eventDepartmentRepository;

    @Autowired
    public CustomSpringEventListener(EventDepartmentRepository eventDepartmentRepository) {
        this.eventDepartmentRepository = eventDepartmentRepository;
    }

    @EventListener(CustomSpringEvent.class)
    public void handleCustom(CustomSpringEvent<EventDepartment> event) {
        eventDepartmentRepository.save(event.getSource());
    }
}