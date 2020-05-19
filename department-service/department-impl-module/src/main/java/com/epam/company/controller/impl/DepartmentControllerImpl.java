package com.epam.company.controller.impl;

import com.epam.company.controller.DepartmentController;
import com.epam.dto.DepartmentDto;
import com.epam.dto.DepartmentDtoReceive;
import com.epam.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epam.company.service.DepartmentService;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("departments")
@RestController
public class DepartmentControllerImpl implements DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentControllerImpl(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public DepartmentDto getDepartment(Long id) {
        return departmentService.getDepartmentInfoById(id);
    }

    public DepartmentDtoReceive addDepartment(DepartmentDtoReceive departmentDto) {
        return departmentService.addDepartment(departmentDto);
    }

    @Override
    public void removeDepartment(Long id) {
        departmentService.removeDepartment(id);
    }

    @Override
    public List<DepartmentDto> getHigherDepartment(Long id) {
        return departmentService.getAllHigherDepartments(id);
    }

    @Override
    public List<DepartmentDto> getAllSubDepartment(Long id) {
        return departmentService.getAllSubordinateDepartments(id);
    }

    @Override
    public List<DepartmentDto> getSubDepartment(Long id) {
        return  departmentService.getSubordinateDepartments(id);
    }

    @Override
    public DepartmentDto changeHeadDepartment(Long idNewHead, Long idCurrent) {
        return departmentService.changeHeadDepartment(idNewHead, idCurrent);
    }

    @Override
    public BigDecimal getSumOfSalary(Long id) {
        return departmentService.getSumOfSalary(id);
    }

    @Override
    public List<EmployeeDto> getEmployeesOfDepartment(Long id) {
        return departmentService.getEmployeesOfDepartment(id);
    }

    @Override
    public DepartmentDto changeTitle(String newTitle, Long id) {
        return departmentService.updateDepartmentTitle(newTitle, id);
    }

    @Override
    public Boolean isExist(Long id) {
        return departmentService.isExist(id);
    }
}
