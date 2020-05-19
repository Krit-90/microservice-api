package com.epam.company.controller.impl;

import com.epam.company.controller.EmployeeController;
import com.epam.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epam.company.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("employees")
@RestController
public class EmployeeControllerImpl implements EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeControllerImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        return employeeService.getEmployeeInfoById(id);
    }

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        return employeeService.addEmployee(employeeDto);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        return employeeService.updateEmployee(employeeDto);
    }

    @Override
    public EmployeeDto firedEmployee(Long id, LocalDate firedDate) {
        return employeeService.firedEmployee(id, firedDate);
    }

    @Override
    public EmployeeDto changeDepartmentOfEmployee(Long employeeId, Long newDepartmentId) {
        return employeeService.changeDepartmentOfEmployee(employeeId, newDepartmentId);
    }

    @Override
    public void changeDepartmentOfAllEmployeeFromSame(Long oldDepartmentId, Long newDepartmentId) {
        employeeService.changeDepartmentOfAllEmployeeFromSame(oldDepartmentId, newDepartmentId);
    }

    @Override
    public EmployeeDto getBossOfEmployee(Long id) {
        return employeeService.getBossOfEmployee(id);
    }

    @Override
    public EmployeeDto getBossOfDepartment(Long id) {
        return employeeService.getBossOfDepartment(id);
    }

    @Override
    public List<EmployeeDto> getEmployeesOfDepartment(Long departmentId) {
        return employeeService.getEmployeesOfDepartment(departmentId);
    }

    @Override
    public Integer getCountEmployeesOfDepartment(Long departmentId) {
        return employeeService.getCountEmployeesOfDepartment(departmentId);
    }

    @Override
    public List<EmployeeDto> getEmployeesByLastNameAndFirstName(String lastName, String firstName) {
        return employeeService.getEmployeesByLastNameAndFirstName(lastName, firstName);
    }
}
