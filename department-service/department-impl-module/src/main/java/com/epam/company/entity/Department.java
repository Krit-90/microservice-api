package com.epam.company.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Table(name = "departments")
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String title;
    @NotNull
    @Column
    private LocalDate creationDate;
    @OneToMany(mappedBy = "headDepartment")
    private Set<Department> subDepartment;
    @ManyToOne
    @JoinColumn(name = "head_department_id")
    private Department headDepartment;


    public Department() {
    }

    public Department(String title) {
        this.title = title;
    }

    public Department(Long id, String title, LocalDate creationDate, Department headDepartment) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.headDepartment = headDepartment;
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

    public Department getHeadDepartment() {
        return headDepartment;
    }

    public void setHeadDepartment(Department headDepartment) {
        this.headDepartment = headDepartment;
    }

    public Set<Department> getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(Set<Department> subDepartment) {
        this.subDepartment = subDepartment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Department{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", subDepartment=").append(subDepartment);
        sb.append(", headDepartment=").append(headDepartment);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;

        Department that = (Department) o;

        if (!getId().equals(that.getId())) return false;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getTitle().hashCode();
        return result;
    }

}
