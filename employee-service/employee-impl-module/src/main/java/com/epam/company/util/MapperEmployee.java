package com.epam.company.util;

import com.epam.dto.EmployeeDto;
import com.epam.company.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperEmployee {
    @Mapping(source = "departmentId", target = "departmentId")
    EmployeeDto employeeToDto(Employee employee);
    @Mapping(source = "departmentId", target = "departmentId")
    Employee DtoToEmployee(EmployeeDto employee);
}