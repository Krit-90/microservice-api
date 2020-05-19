package com.epam.dto;


import java.time.LocalDate;

public class DepartmentDto {
    private Long id;
    private String title;
    private LocalDate creationDate;
    private EmployeeDto boss;
    private Integer countEmployee;

    public DepartmentDto() {
    }

    public DepartmentDto(Long id, String title, LocalDate creationDate, EmployeeDto boss, Integer countEmployee) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.boss = boss;
        this.countEmployee = countEmployee;
    }

    public DepartmentDto(String title) {
        this.title = title;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public EmployeeDto getBoss() {
        return boss;
    }

    public void setBoss(EmployeeDto boss) {
        this.boss = boss;
    }

    public Integer getCountEmployee() {
        return countEmployee;
    }

    public void setCountEmployee(Integer countEmployee) {
        this.countEmployee = countEmployee;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentDto{");
        sb.append("title='").append(title).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", boss=").append(boss);
        sb.append(", countEmployee=").append(countEmployee);
        sb.append('}');
        return sb.toString();
    }
}
