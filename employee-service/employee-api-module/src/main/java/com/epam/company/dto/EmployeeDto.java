package com.epam.company.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto {
    private Long id;
    @NotNull
    @Pattern(regexp = "^[а-яА-Я\\-]+")
    private String lastName;
    @NotNull
    @Pattern(regexp = "^[а-яА-Я\\-]+")
    private String firstName;
    @Pattern(regexp = "^[а-яА-Я\\-]+")
    private String patronymic;
    @NotNull
    private Sex gender;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    @Pattern(regexp = "^[0-9\\-+()]+")
    private String phone;
    @NotNull
    @Email
    private String email;
    @NotNull
    private LocalDate employmentDate;
    private LocalDate firedDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;
    @NotNull
    private BigDecimal salary;
    private Boolean isBoss;
    @NotNull
    private Long departmentId;

    public EmployeeDto() {
    }

    public EmployeeDto(Long id, String lastName, String firstName, String patronymic, Sex gender, LocalDate birthDate,
                       String phone, String email, LocalDate employmentDate, LocalDate firedDate, JobTitle jobTitle,
                       BigDecimal salary, Boolean isBoss) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.employmentDate = employmentDate;
        this.firedDate = firedDate;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.isBoss = isBoss;
    }

    public EmployeeDto(String lastName, String firstName, String patronymic, Sex gender, LocalDate birthDate,
                       String phone, String email, LocalDate employmentDate, LocalDate firedDate, JobTitle jobTitle,
                       BigDecimal salary, Boolean isBoss, Long departmentId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.employmentDate = employmentDate;
        this.firedDate = firedDate;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.isBoss = isBoss;
        this.departmentId = departmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Sex getGender() {
        return gender;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public LocalDate getFiredDate() {
        return firedDate;
    }

    public void setFiredDate(LocalDate firedDate) {
        this.firedDate = firedDate;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Boolean getBoss() {
        return isBoss;
    }

    public void setBoss(Boolean boss) {
        isBoss = boss;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDto)) return false;

        EmployeeDto that = (EmployeeDto) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        if (getPatronymic() != null ? !getPatronymic().equals(that.getPatronymic()) : that.getPatronymic() != null)
            return false;
        if (getGender() != that.getGender()) return false;
        if (getBirthDate() != null ? !getBirthDate().equals(that.getBirthDate()) : that.getBirthDate() != null)
            return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getEmploymentDate() != null ? !getEmploymentDate().equals(that.getEmploymentDate()) : that.getEmploymentDate() != null)
            return false;
        if (getFiredDate() != null ? !getFiredDate().equals(that.getFiredDate()) : that.getFiredDate() != null)
            return false;
        if (getJobTitle() != that.getJobTitle()) return false;
        if (getSalary() != null ? !getSalary().equals(that.getSalary()) : that.getSalary() != null) return false;
        if (isBoss != null ? !isBoss.equals(that.isBoss) : that.isBoss != null) return false;
        return getDepartmentId() != null ? getDepartmentId().equals(that.getDepartmentId()) : that.getDepartmentId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getPatronymic() != null ? getPatronymic().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getBirthDate() != null ? getBirthDate().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getEmploymentDate() != null ? getEmploymentDate().hashCode() : 0);
        result = 31 * result + (getFiredDate() != null ? getFiredDate().hashCode() : 0);
        result = 31 * result + (getJobTitle() != null ? getJobTitle().hashCode() : 0);
        result = 31 * result + (getSalary() != null ? getSalary().hashCode() : 0);
        result = 31 * result + (isBoss != null ? isBoss.hashCode() : 0);
        result = 31 * result + (getDepartmentId() != null ? getDepartmentId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EmployeeDto{");
        sb.append("lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", patronymic='").append(patronymic).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", employmentDate=").append(employmentDate);
        sb.append(", firedDate=").append(firedDate);
        sb.append(", jobTitle=").append(jobTitle);
        sb.append(", salary=").append(salary);
        sb.append(", isBoss=").append(isBoss);
        sb.append('}');
        return sb.toString();
    }
}
