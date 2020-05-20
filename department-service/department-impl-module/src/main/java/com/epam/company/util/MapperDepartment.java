package com.epam.company.util;

import com.epam.company.dto.DepartmentDto;
import com.epam.company.dto.DepartmentDtoReceive;
import com.epam.company.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = LocalDate.class)
public interface MapperDepartment {
    DepartmentDto departmentToDto(Department department);
    Department DtoToDepartment(DepartmentDto departmentDto);
    @Mapping(source = "department.headDepartment.id", target = "headId")
    DepartmentDtoReceive departmentToDtoReceive(Department department);
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    Department DtoReceiveToDepartment(DepartmentDtoReceive departmentDtoReceive);
}