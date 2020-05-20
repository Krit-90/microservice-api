package com.epam.company.dto;

import javax.validation.constraints.NotNull;

public class DepartmentDtoReceive {
    private Long id;
    @NotNull
    private String title;
    private Long headId;

    public DepartmentDtoReceive() {
    }

    public DepartmentDtoReceive(String title, Long headId) {
        this.title = title;
        this.headId = headId;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
