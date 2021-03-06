package com.epam.company.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "department_monitoring")
@Entity
public class EventDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Enumerated(EnumType.STRING)
    private EventTitle event;
    @Column
    private Long departmentId;
    @Column
    private LocalDateTime dateTime;

    public EventDepartment() {
    }

    public EventDepartment(Long id, EventTitle event, Long departmentId, LocalDateTime dateTime) {
        this.id = id;
        this.event = event;
        this.departmentId = departmentId;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventTitle getEvent() {
        return event;
    }

    public void setEvent(EventTitle event) {
        this.event = event;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventDepartment{");
        sb.append("id=").append(id);
        sb.append(", event=").append(event);
        sb.append(", departmentId=").append(departmentId);
        sb.append(", dateTime=").append(dateTime);
        sb.append('}');
        return sb.toString();
    }
}
