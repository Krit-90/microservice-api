package com.epam.company.util;

import com.epam.company.controller.EmployeeController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("employee-service")
public interface EmployeeControllerFeign extends EmployeeController {
}
