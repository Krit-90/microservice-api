package com.epam.company.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "departments_fund")
@Entity
public class DepartmentFund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private BigDecimal sumSalaries;
    @Column
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
