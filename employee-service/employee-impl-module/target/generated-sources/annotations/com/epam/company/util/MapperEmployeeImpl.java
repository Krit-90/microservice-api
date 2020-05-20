package com.epam.company.util;

import com.epam.company.dto.EmployeeDto;
import com.epam.company.dto.JobTitle;
import com.epam.company.dto.Sex;
import com.epam.company.entity.Employee;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-20T20:33:26+0400",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class MapperEmployeeImpl implements MapperEmployee {

    @Override
    public EmployeeDto employeeToDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setDepartmentId( employee.getDepartmentId() );
        employeeDto.setId( employee.getId() );
        employeeDto.setLastName( employee.getLastName() );
        employeeDto.setFirstName( employee.getFirstName() );
        employeeDto.setPatronymic( employee.getPatronymic() );
        employeeDto.setGender( sexToSex( employee.getGender() ) );
        employeeDto.setBirthDate( employee.getBirthDate() );
        employeeDto.setPhone( employee.getPhone() );
        employeeDto.setEmail( employee.getEmail() );
        employeeDto.setEmploymentDate( employee.getEmploymentDate() );
        employeeDto.setFiredDate( employee.getFiredDate() );
        employeeDto.setJobTitle( jobTitleToJobTitle( employee.getJobTitle() ) );
        employeeDto.setSalary( employee.getSalary() );
        employeeDto.setBoss( employee.getBoss() );

        return employeeDto;
    }

    @Override
    public Employee DtoToEmployee(EmployeeDto employee) {
        if ( employee == null ) {
            return null;
        }

        Employee employee1 = new Employee();

        employee1.setDepartmentId( employee.getDepartmentId() );
        employee1.setId( employee.getId() );
        employee1.setLastName( employee.getLastName() );
        employee1.setFirstName( employee.getFirstName() );
        employee1.setPatronymic( employee.getPatronymic() );
        employee1.setGender( sexToSex1( employee.getGender() ) );
        employee1.setBirthDate( employee.getBirthDate() );
        employee1.setPhone( employee.getPhone() );
        employee1.setEmail( employee.getEmail() );
        employee1.setEmploymentDate( employee.getEmploymentDate() );
        employee1.setFiredDate( employee.getFiredDate() );
        employee1.setJobTitle( jobTitleToJobTitle1( employee.getJobTitle() ) );
        employee1.setSalary( employee.getSalary() );
        employee1.setBoss( employee.getBoss() );

        return employee1;
    }

    protected Sex sexToSex(com.epam.company.entity.Sex sex) {
        if ( sex == null ) {
            return null;
        }

        Sex sex1;

        switch ( sex ) {
            case MALE: sex1 = Sex.MALE;
            break;
            case FEMALE: sex1 = Sex.FEMALE;
            break;
            case NONE: sex1 = Sex.NONE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + sex );
        }

        return sex1;
    }

    protected JobTitle jobTitleToJobTitle(com.epam.company.entity.JobTitle jobTitle) {
        if ( jobTitle == null ) {
            return null;
        }

        JobTitle jobTitle1;

        switch ( jobTitle ) {
            case BOSS: jobTitle1 = JobTitle.BOSS;
            break;
            case COMMON: jobTitle1 = JobTitle.COMMON;
            break;
            case NONE: jobTitle1 = JobTitle.NONE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + jobTitle );
        }

        return jobTitle1;
    }

    protected com.epam.company.entity.Sex sexToSex1(Sex sex) {
        if ( sex == null ) {
            return null;
        }

        com.epam.company.entity.Sex sex1;

        switch ( sex ) {
            case MALE: sex1 = com.epam.company.entity.Sex.MALE;
            break;
            case FEMALE: sex1 = com.epam.company.entity.Sex.FEMALE;
            break;
            case NONE: sex1 = com.epam.company.entity.Sex.NONE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + sex );
        }

        return sex1;
    }

    protected com.epam.company.entity.JobTitle jobTitleToJobTitle1(JobTitle jobTitle) {
        if ( jobTitle == null ) {
            return null;
        }

        com.epam.company.entity.JobTitle jobTitle1;

        switch ( jobTitle ) {
            case BOSS: jobTitle1 = com.epam.company.entity.JobTitle.BOSS;
            break;
            case COMMON: jobTitle1 = com.epam.company.entity.JobTitle.COMMON;
            break;
            case NONE: jobTitle1 = com.epam.company.entity.JobTitle.NONE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + jobTitle );
        }

        return jobTitle1;
    }
}
