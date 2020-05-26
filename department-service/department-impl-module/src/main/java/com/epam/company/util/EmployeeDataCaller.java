package com.epam.company.util;

import com.epam.company.dto.EmployeeDto;
import com.epam.company.dto.JobTitle;
import com.epam.company.dto.Sex;
import com.epam.company.entity.Department;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class EmployeeDataCaller {
    private final EmployeeControllerFeign employeeControllerFeign;

    @Autowired
    public EmployeeDataCaller(EmployeeControllerFeign employeeControllerFeign) {
        this.employeeControllerFeign = employeeControllerFeign;
    }

    @HystrixCommand(fallbackMethod = "fallbackEmployees")
    public List<EmployeeDto> getEmployees(@NonNull Long id) {
        return employeeControllerFeign.getEmployeesOfDepartment(id);
    }

    private List<EmployeeDto> fallbackEmployees(@NonNull Long id) {
        return Collections.singletonList(fallbackBoss(null));
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
    }

    private EmployeeDto fallbackBoss(Department department) {
        return new EmployeeDto(-1L, "н/д", "н/д", "н/д", Sex.NONE,
                null, "-", "н/д",
                null, null,
                JobTitle.NONE, BigDecimal.ZERO, false);
    }

}
