package com.epam.company.util;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.ConnectException;

@Component
public class DepartmentDataCaller {

    private final DepartmentControllerFeign departmentControllerFeign;
    private final RestTemplate restTemplate;
    private final String DEPARTMENT_URL = "http://department-service/departments/";

    @Autowired
    public DepartmentDataCaller(DepartmentControllerFeign departmentControllerFeign, RestTemplate restTemplate) {
        this.departmentControllerFeign = departmentControllerFeign;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallbackDepartmentExist")
    public Boolean isDepartmentExist(@NonNull Long newDepartmentId) {
        return departmentControllerFeign.isExist(newDepartmentId);
    }

    // TODO: выбросить в методе без true/false или просто ошибку?
    private Boolean fallbackDepartmentExist(@NonNull Long newDepartmentId) throws ConnectException {
        throw new ConnectException("Сервис departments недоступен");
    }
}
