package com.epam.company.util;

import com.epam.company.controller.DepartmentController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("department-service")
public interface DepartmentControllerFeign extends DepartmentController {
}
