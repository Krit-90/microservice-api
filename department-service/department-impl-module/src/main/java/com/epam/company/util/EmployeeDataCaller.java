package com.epam.company.util;

import com.epam.company.entity.Department;
import com.epam.company.dto.EmployeeDto;
import com.epam.company.dto.JobTitle;
import com.epam.company.dto.Sex;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class EmployeeDataCaller {
    private final EmployeeControllerFeign employeeControllerFeign;
    private final RestTemplate restTemplate;
    private final String EMPLOYEE_URL = "http://employee-service/employees/";

    @Autowired
    public EmployeeDataCaller(EmployeeControllerFeign employeeControllerFeign, RestTemplate restTemplate) {
        this.employeeControllerFeign = employeeControllerFeign;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallbackEmployees")
    public List<EmployeeDto> getEmployees(@NonNull Long id) {
        return employeeControllerFeign.getEmployeesOfDepartment(id);
    }

    private List<EmployeeDto> fallbackEmployees(@NonNull Long id) {
        return Arrays.asList(fallbackBoss(null));
    }

    @HystrixCommand(fallbackMethod = "fallbackCountEmployees")
    public Integer getCountEmployees(Department department) {
        return employeeControllerFeign.getCountEmployeesOfDepartment(department.getId());
    }

    private Integer fallbackCountEmployees(Department department) {
        return -1;
    }

    @HystrixCommand(fallbackMethod = "fallbackBoss")
    public EmployeeDto getBoss(Department department) {
        return employeeControllerFeign.getBossOfDepartment(department.getId());
/*        return restTemplate
                .getForObject(EMPLOYEE_URL + department.getId() + "/boss", EmployeeDto.class);*/
    }

    private EmployeeDto fallbackBoss(Department department) {
        return new EmployeeDto(-1L, "н/д", "н/д", "н/д", Sex.NONE,
                null, "-", "н/д",
                null, null,
                JobTitle.NONE, BigDecimal.ZERO, false);
    }

}
