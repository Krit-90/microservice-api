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
    private String phone;
    @NotNull
    @Email
    private String email;
    private LocalDate employmentDate;
    private LocalDate firedDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;
    @NotNull
    private BigDecimal salary;
    private Boolean isBoss;
    private String departmentTitle;
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
                       BigDecimal salary, Boolean isBoss, String departmentTitle, Long departmentId) {
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
        this.departmentTitle = departmentTitle;
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

    public String getDepartmentTitle() {
        return departmentTitle;
    }

    public void setDepartmentTitle(String departmentTitle) {
        this.departmentTitle = departmentTitle;
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
        sb.append(", department=").append(departmentTitle);
        sb.append('}');
        return sb.toString();
    }
}
