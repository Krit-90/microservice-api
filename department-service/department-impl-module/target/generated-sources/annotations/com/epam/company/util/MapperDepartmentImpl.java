package com.epam.company.util;

import com.epam.company.dto.DepartmentDto;
import com.epam.company.dto.DepartmentDtoReceive;
import com.epam.company.entity.Department;
import java.time.LocalDate;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-20T20:28:04+0400",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class MapperDepartmentImpl implements MapperDepartment {

    @Override
    public DepartmentDto departmentToDto(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDto departmentDto = new DepartmentDto();

        departmentDto.setId( department.getId() );
        departmentDto.setTitle( department.getTitle() );
        departmentDto.setCreationDate( department.getCreationDate() );

        return departmentDto;
    }

    @Override
    public Department DtoToDepartment(DepartmentDto departmentDto) {
        if ( departmentDto == null ) {
            return null;
        }

        Department department = new Department();

        department.setId( departmentDto.getId() );
        department.setTitle( departmentDto.getTitle() );
        department.setCreationDate( departmentDto.getCreationDate() );

        return department;
    }

    @Override
    public DepartmentDtoReceive departmentToDtoReceive(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDtoReceive departmentDtoReceive = new DepartmentDtoReceive();

        Long id = departmentHeadDepartmentId( department );
        if ( id != null ) {
            departmentDtoReceive.setHeadId( id );
        }
        departmentDtoReceive.setId( department.getId() );
        departmentDtoReceive.setTitle( department.getTitle() );

        return departmentDtoReceive;
    }

    @Override
    public Department DtoReceiveToDepartment(DepartmentDtoReceive departmentDtoReceive) {
        if ( departmentDtoReceive == null ) {
            return null;
        }

        Department department = new Department();

        department.setId( departmentDtoReceive.getId() );
        department.setTitle( departmentDtoReceive.getTitle() );

        department.setCreationDate( LocalDate.now() );

        return department;
    }

    private Long departmentHeadDepartmentId(Department department) {
        if ( department == null ) {
            return null;
        }
        Department headDepartment = department.getHeadDepartment();
        if ( headDepartment == null ) {
            return null;
        }
        Long id = headDepartment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
