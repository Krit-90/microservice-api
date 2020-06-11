package com.epam.company.entity;

import java.math.BigDecimal;

public class DepartmentFund {
    private Long id;
    private BigDecimal sumSalaries;
    private Long departmentId;

    public DepartmentFund() {
    }

    public DepartmentFund(BigDecimal sumSalaries, Long departmentId) {
        this.sumSalaries = sumSalaries;
        this.departmentId = departmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSumSalaries() {
        return sumSalaries;
    }

    public void setSumSalaries(BigDecimal sumSalaries) {
        this.sumSalaries = sumSalaries;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentFund{");
        sb.append("id=").append(id);
        sb.append(", sumSalaries=").append(sumSalaries);
        sb.append(", departmentId=").append(departmentId);
        sb.append('}');
        return sb.toString();
    }
}
