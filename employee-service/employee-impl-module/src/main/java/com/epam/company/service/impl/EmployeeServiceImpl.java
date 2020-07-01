package com.epam.company.service.impl;

import com.epam.company.dto.EmployeeDto;
import com.epam.company.entity.Employee;
import com.epam.company.exception.NoSuchElementInDBException;
import com.epam.company.repository.DepartmentSnapshotRepository;
import com.epam.company.repository.EmployeeRepository;
import com.epam.company.service.EmployeeService;
import com.epam.company.util.DepartmentDataCaller;
import com.epam.company.util.MapperEmployee;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final DepartmentSnapshotRepository departmentSnapshotRepository;

    @Autowired
    public EmployeeServiceImpl(DepartmentDataCaller departmentDataCaller, Validator validator,
                               EmployeeRepository employeeRepository, MapperEmployee mapperEmployee,
                               DepartmentSnapshotRepository departmentSnapshotRepository) {
        this.departmentDataCaller = departmentDataCaller;
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.mapperEmployee = mapperEmployee;
        this.departmentSnapshotRepository = departmentSnapshotRepository;
    }
    @Override
    public EmployeeDto addOrUpdateEmployee(@NonNull EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            employeeRepository.findById(employeeDto.getId())
                    .orElseThrow(() -> new NoSuchElementInDBException("Работник не найден"));
        }
        validateOrThrow(employeeDto);
        Employee employee = mapperEmployee.DtoToEmployee(employeeDto);
        if (employeeDto.getId() == null) {
            employeeRepository.save(employee);

        } else {
            employeeRepository.update(employee);
        }
        employeeDto = mapperEmployee.employeeToDto(employee);
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
        employeeRepository.update(employee);
        return mapperEmployee.employeeToDto(employee);
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
        Boolean isDepartmentExist = departmentSnapshotRepository.isExist(newDepartmentId).orElse(false)
                ? Boolean.valueOf(true) : departmentDataCaller.isDepartmentExist(newDepartmentId);

        if (isDepartmentExist != null && !isDepartmentExist) {
            throw new NoSuchElementInDBException("Департамент не найден");
        }
        employee.setDepartmentId(newDepartmentId);
        EmployeeDto employeeDto = mapperEmployee.employeeToDto(employee);
        validateOrThrow(employeeDto);
        employeeRepository.update(employee);
        return employeeDto;
    }

    @Transactional
    @Override
    public void changeDepartmentOfAllEmployeeFromSame(@NonNull Long oldDepartmentId, @NonNull Long newDepartmentId) {
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

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void saveDepartmentSnapshot(String departmentSnapshot) {
        String[] data = departmentSnapshot.split("/");
        if (departmentSnapshotRepository.isExist(Long.valueOf(data[0])).isPresent()) {
            departmentSnapshotRepository.update(Long.valueOf(data[0]), Boolean.parseBoolean(data[1]));
        } else {
            departmentSnapshotRepository.save(Long.valueOf(data[0]), Boolean.parseBoolean(data[1]));
        }
    }

    private Set<ConstraintViolation<EmployeeDto>> validateOrThrow(EmployeeDto employeeDto) {
        Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employeeDto);
        if (!violations.isEmpty()) {
            StringBuilder error = new StringBuilder();
            violations.forEach(exc -> error.append(exc.getMessage()).
                    append(" ").
                    append(exc.getPropertyPath()).append("; "));
            throw new ValidationException(String.valueOf(error));
        }
        Boolean isDepartmentExist = departmentSnapshotRepository.isExist(employeeDto.getDepartmentId()).
                orElse(false)
                ? Boolean.valueOf(true) : departmentDataCaller.isDepartmentExist(employeeDto.getDepartmentId());
        if (!isDepartmentExist) {
            throw new ValidationException("Данного депаратамента не существует");
        }
        if (employeeDto.getFiredDate() != null) {
            throw new ValidationException("При создании или обновлении не должно быть даты увольнения");
        }
        if (!employeeRepository.getBossOfDepartment(employeeDto.getDepartmentId())
                .equals(employeeRepository.findById(employeeDto.getId()).orElse(null)) &&
                employeeDto.getBoss() && employeeRepository.countBossOfDepartment(employeeDto.getDepartmentId()) > 0) {
            throw new ValidationException("Может быть лишь один руководитель");
        }
        if (!employeeDto.getBoss() && employeeDto.getSalary().compareTo(
                employeeRepository.getBossOfDepartment(employeeDto.getDepartmentId()).getSalary()) > 0) {
            throw new ValidationException("Зарплата не может быть больше, чем у руководителя");
        }
        if (employeeDto.getEmploymentDate().isBefore(employeeDto.getBirthDate())) {
            throw new ValidationException("Дата приема на работу, должна быть после дня рождения");
        }
        return violations;
    }

}
