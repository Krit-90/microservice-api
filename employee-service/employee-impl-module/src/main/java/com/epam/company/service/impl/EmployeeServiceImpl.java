package com.epam.company.service.impl;

import com.epam.company.util.DepartmentDataCaller;
import com.epam.company.dto.EmployeeDto;
import com.epam.company.entity.Employee;
import com.epam.company.exception.NoSuchElementInDBException;
import lombok.NonNull;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.company.repository.EmployeeRepository;
import com.epam.company.service.EmployeeService;
import com.epam.company.util.MapperEmployee;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final DepartmentDataCaller departmentDataCaller;
    private final Validator validator;
    private final EmployeeRepository employeeRepository;
    private final MapperEmployee mapperEmployee;

    @Autowired
    public EmployeeServiceImpl(DepartmentDataCaller departmentDataCaller, Validator validator,
                               EmployeeRepository employeeRepository, RestTemplate restTemplate) {
        this.departmentDataCaller = departmentDataCaller;
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.mapperEmployee = Mappers.getMapper(MapperEmployee.class);
    }

    @Override
    public EmployeeDto addOrUpdateEmployee(@NonNull EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        }
        validateOrThrow(employeeDto);
        Employee employee = mapperEmployee.DtoToEmployee(employeeDto);
        employeeDto = mapperEmployee.employeeToDto(employeeRepository.save(employee));
        return employeeDto;
    }

    @Override
    public void removeEmployee(@NonNull Long id) {
        if (employeeRepository.findById(id).orElse(null) == null) {
            return;
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeDto firedEmployee(@NonNull Long id, @NonNull LocalDate firedDate) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        if (employee.getEmploymentDate().isAfter(firedDate)) {
            throw new ValidationException("Дата увольнения, должна быть после приема на работу");
        }
        employee.setFiredDate(firedDate);
        employee.setDepartmentId(null);
        return mapperEmployee.employeeToDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto getEmployeeInfoById(@NonNull Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        return mapperEmployee.employeeToDto(employee);
    }

    @Override
    public EmployeeDto changeDepartmentOfEmployee(@NonNull Long employeeId, @NonNull Long newDepartmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        Boolean isDepartmentExist = departmentDataCaller.isDepartmentExist(newDepartmentId);
        if (isDepartmentExist != null && !isDepartmentExist) {
            throw new NoSuchElementInDBException("Департамент не найден");
        }
        employee.setDepartmentId(newDepartmentId);
        EmployeeDto employeeDto = mapperEmployee.employeeToDto(employee);
        validateOrThrow(employeeDto);
        employeeRepository.save(employee);
        return employeeDto;
    }

    @Transactional
    @Override
    public void changeDepartmentOfAllEmployeeFromSame(@NonNull Long oldDepartmentId, @NonNull Long newDepartmentId) {
        Boolean isDepartmentExist = departmentDataCaller.isDepartmentExist(oldDepartmentId);
        if (isDepartmentExist != null && !isDepartmentExist) {
            throw new NoSuchElementInDBException("Департамент не найден");
        }
        employeeRepository.findByDepartmentId(oldDepartmentId)
                .forEach(employee -> changeDepartmentOfEmployee(employee.getId(), newDepartmentId));
    }

    @Override
    public EmployeeDto getBossOfEmployee(@NonNull Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        return mapperEmployee.employeeToDto(employeeRepository.getBossOfDepartment(employee.getDepartmentId()));
    }

    @Override
    public EmployeeDto getBossOfDepartment(@NonNull Long id) {
        return mapperEmployee.employeeToDto(employeeRepository.getBossOfDepartment(id));
    }

    @Override
    public List<EmployeeDto> getEmployeesOfDepartment(Long id) {
        ArrayList<EmployeeDto> employeesDtoList = new ArrayList<>();
        employeeRepository.findByDepartmentId(id)
                .forEach(employee -> employeesDtoList.add(mapperEmployee.employeeToDto(employee)));
        return employeesDtoList;
    }

    @Override
    public List<EmployeeDto> getEmployeesByLastNameAndFirstName(@NonNull String lastName, @NonNull String firstName) {
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeRepository.findByLastNameAndFirstName(lastName, firstName)
                .forEach(employee -> employeeDtos.add(mapperEmployee.employeeToDto(employee)));
        return employeeDtos;
    }

    @Override
    public Integer getCountEmployeesOfDepartment(Long id) {
        return employeeRepository.countIdByDepartmentId(id);
    }

    private Set<ConstraintViolation<EmployeeDto>> validateOrThrow(EmployeeDto employeeDto) {
        Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employeeDto);
        Boolean isDepartmentExist = departmentDataCaller.isDepartmentExist(employeeDto.getDepartmentId());
        if (!isDepartmentExist) {
            throw new ValidationException("Данного депаратмента не существует");
        }
        if (employeeDto.getFiredDate() != null) {
            throw new ValidationException("При создании или обновлении не должно быть даты увольения");
        }
        if (employeeDto.getBoss() && employeeRepository.countBossOfDepartment(employeeDto.getDepartmentId()) > 0) {
            throw new ValidationException("Может быть лишь один руководитель");
        }
        if (!employeeDto.getBoss() && employeeDto.getSalary() != null && employeeDto.getSalary().compareTo(
                employeeRepository.getBossOfDepartment(employeeDto.getDepartmentId()).getSalary()) > 0) {
            throw new ValidationException("Зарплата не может быть больше, чем у руководителя");
        }
        if (employeeDto.getEmploymentDate() != null && employeeDto.getBirthDate() != null &&
                employeeDto.getEmploymentDate().isBefore(employeeDto.getBirthDate())) {
            throw new ValidationException("Дата приема на работу, должна быть после дня рождения");
        }
        return violations;
    }

}
