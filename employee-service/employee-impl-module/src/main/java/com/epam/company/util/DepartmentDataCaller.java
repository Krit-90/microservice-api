package com.epam.company.util;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.ConnectException;

@Component
public class DepartmentDataCaller {

    private final DepartmentControllerFeign departmentControllerFeign;

    @Autowired
    public DepartmentDataCaller(DepartmentControllerFeign departmentControllerFeign) {
        this.departmentControllerFeign = departmentControllerFeign;
    }

    @HystrixCommand(fallbackMethod = "fallbackDepartmentExist")
    public Boolean isDepartmentExist(@NonNull Long newDepartmentId) {
        return departmentControllerFeign.isExist(newDepartmentId);
    }

    private Boolean fallbackDepartmentExist(@NonNull Long newDepartmentId) throws ConnectException {
        throw new ConnectException("Сервис departments недоступен");
    }
}
