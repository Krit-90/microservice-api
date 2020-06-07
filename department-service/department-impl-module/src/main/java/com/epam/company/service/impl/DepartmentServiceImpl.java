package com.epam.company.service.impl;

import com.epam.company.dto.DepartmentDto;
import com.epam.company.dto.DepartmentDtoReceive;
import com.epam.company.dto.EmployeeDto;
import com.epam.company.entity.Department;
import com.epam.company.entity.DepartmentFund;
import com.epam.company.entity.EventDepartment;
import com.epam.company.entity.EventTitle;
import com.epam.company.exception.NoSuchElementInDBException;
import com.epam.company.repository.DepartmentFundRepository;
import com.epam.company.repository.DepartmentRepository;
import com.epam.company.service.DepartmentService;
import com.epam.company.util.CustomSpringEvent;
import com.epam.company.util.CustomSpringEventPublisher;
import com.epam.company.util.EmployeeDataCaller;
import com.epam.company.util.MapperDepartment;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final MapperDepartment mapperDepartment;
    private final EmployeeDataCaller employeeDataCaller;
    private final DepartmentFundRepository departmentFundRepository;
    private final CustomSpringEventPublisher customSpringEventPublisher;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, MapperDepartment mapperDepartment,
                                 EmployeeDataCaller employeeDataCaller, DepartmentFundRepository departmentFundRepository,
                                 CustomSpringEventPublisher customSpringEventPublisher) {
        this.departmentRepository = departmentRepository;
        this.mapperDepartment = mapperDepartment;
        this.employeeDataCaller = employeeDataCaller;
        this.departmentFundRepository = departmentFundRepository;
        this.customSpringEventPublisher = customSpringEventPublisher;
    }

    @Transactional
    @Override
    public DepartmentDtoReceive addDepartment(@NonNull DepartmentDtoReceive departmentDtoReceive) {
        Department headDepartment = null;
        if (departmentRepository.findByTitle(departmentDtoReceive.getTitle()).isPresent()) {
            throw new ValidationException("Департамент с данным названием уже существует");
        }
        if (departmentDtoReceive.getHeadId() == null && departmentRepository.countByHeadDepartmentIsNull() > 0) {
            throw new NoSuchElementInDBException("Самый верхний депаратмент уже существует");
        } else {
            if (departmentDtoReceive.getHeadId() != null) {
                headDepartment = departmentRepository.findById(departmentDtoReceive.getHeadId())
                        .orElseThrow(() -> new NoSuchElementInDBException("Вышестоящий департамент не найден"));
            }
        }
        Department department = mapperDepartment.DtoReceiveToDepartment(departmentDtoReceive);
        department.setHeadDepartment(headDepartment);
        departmentRepository.save(department);
        customSpringEventPublisher.publishEvent(new CustomSpringEvent<>(
                new EventDepartment(null, EventTitle.CREATE, department.getId(), LocalDateTime.now())));
        return mapperDepartment.departmentToDtoReceive(department);
    }

    @Transactional
    @Override
    public DepartmentDto updateDepartmentTitle(String newTitle, @NonNull Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден"));
        if (departmentRepository.findByTitle(newTitle).isPresent()) {
            throw new ValidationException("Департамент с данным названием уже существует");
        }
        department.setTitle(newTitle);
        customSpringEventPublisher.publishEvent(new CustomSpringEvent<>(
                new EventDepartment(null, EventTitle.EDIT, department.getId(), LocalDateTime.now())));
        return enrichDepartmentDto(departmentRepository.save(department));
    }

    @Transactional
    @Override
    public void removeDepartment(@NonNull Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            return;
        }
        if (!getEmployeesOfDepartment(id).isEmpty()) {
            throw new ValidationException("Невозможно удаление департамента при наличии работников");
        }
        customSpringEventPublisher.publishEvent(new CustomSpringEvent<>(
                new EventDepartment(null, EventTitle.DELETE, department.getId(), LocalDateTime.now())));
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDto getDepartmentInfoById(@NonNull Long id) {
        return enrichDepartmentDto(departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден")));
    }

    @Transactional
    @Override
    public DepartmentDto changeHeadDepartment(@NonNull Long idNewHead, @NonNull Long idCurrent) {
        if (idCurrent.equals(idNewHead)) {
            throw new ValidationException("Департамент не может быть главныи для самого себя");
        }
        Department department = departmentRepository.findById(idCurrent).orElseThrow(() ->
                new NoSuchElementInDBException("Департамент не найден"));
        Department headDepartment = departmentRepository.findById(idNewHead).orElseThrow(() ->
                new NoSuchElementInDBException("Департамент не найден"));
        department.setHeadDepartment(headDepartment);
        customSpringEventPublisher.publishEvent(new CustomSpringEvent<>(
                new EventDepartment(null, EventTitle.EDIT, department.getId(), LocalDateTime.now())));
        return enrichDepartmentDto(departmentRepository.save(department));
    }

    @Override
    public DepartmentDto getDepartmentByTitle(@NonNull String title) {
        return enrichDepartmentDto(departmentRepository.findByTitle(title).
                orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден")));
    }

    @Override
    public List<DepartmentDto> getAllHigherDepartments(@NonNull Long id) {
        List<DepartmentDto> higherDepartments = new ArrayList<>();
        Department current = departmentRepository.findById(id).orElseThrow(() ->
                new NoSuchElementInDBException("Дапартамент не найден"));
        current = current.getHeadDepartment();
        while (current != null) {
            higherDepartments.add(enrichDepartmentDto(current));
            current = current.getHeadDepartment();
        }
        return higherDepartments;
    }

    @Override
    public List<DepartmentDto> getAllSubordinateDepartments(@NonNull Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Дапартамент не найден"));
        List<Department> subDepartments = new ArrayList<>();
        DepartmentIterator departmentIterator = new DepartmentIterator(department);
        while (departmentIterator.hasNext()) {
            subDepartments.add(departmentIterator.next());
        }
        List<DepartmentDto> subDepartmentsDto = new ArrayList<>();
        subDepartments.forEach(dep -> subDepartmentsDto.add(enrichDepartmentDto(dep)));
        return subDepartmentsDto;
    }

    @Override
    public List<DepartmentDto> getSubordinateDepartments(@NonNull Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден"));
        List<DepartmentDto> departmentsDtoList = new ArrayList<>();
        department.getSubDepartment().forEach(dep -> departmentsDtoList.add(enrichDepartmentDto(dep)));
        return departmentsDtoList;
    }

    @Override
    public BigDecimal getSumOfSalary(@NonNull Long id) {
        BigDecimal sumOfSalary = new BigDecimal(0);
        departmentRepository.findById(id).orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден"));
        List<EmployeeDto> employees = getEmployeesOfDepartment(id);
        for (EmployeeDto employee : employees) {
            sumOfSalary = sumOfSalary.add(employee.getSalary());
        }
        return sumOfSalary;
    }

    @Override
    public List<EmployeeDto> getEmployeesOfDepartment(@NonNull Long id) {
        departmentRepository.findById(id).orElseThrow(() -> new NoSuchElementInDBException("Департамент не найден"));
        return employeeDataCaller.getEmployees(id);
    }

    @Override
    public Boolean isExist(Long id) {
        return departmentRepository.findById(id).isPresent();
    }

    @Transactional
    @Scheduled(fixedRateString = "${fund.rate}")
    public void fillDepartmentsFundTable() {
        List<Long> allDepartmentsId = departmentRepository.findAll()
                .stream().map(Department::getId).collect(Collectors.toList());
        departmentFundRepository.deleteAll();
        for (Long id : allDepartmentsId) {
            departmentFundRepository.save(new DepartmentFund(getSumOfSalary(id), id));
        }
    }

    public void dumpLoad() {
        String query = "Insert Into departments (title, creation_date, head_department_id) Values (?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(
                new FileReader("department-service/department-impl-module/src/main/resources/dump.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split("/");
                jdbcTemplate.update(query, row[0], LocalDate.parse(row[1]), Long.valueOf(row[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DepartmentDto enrichDepartmentDto(Department department) {
        DepartmentDto departmentDto = mapperDepartment.departmentToDto(department);
        departmentDto.setBoss(employeeDataCaller.getBoss(department));
        departmentDto.setCountEmployee(employeeDataCaller.getCountEmployees(department));
        return departmentDto;
    }

    private static class DepartmentIterator implements Iterator<Department> {
        Department nextDepartment;
        Queue<Department> queue = new LinkedList<>();

        public DepartmentIterator(Department department) {
            queue.addAll(department.getSubDepartment());
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Department next() {
            nextDepartment = queue.poll();
            assert nextDepartment != null;
            if (nextDepartment.getSubDepartment() != null) {
                queue.addAll(nextDepartment.getSubDepartment());
            }
            return nextDepartment;
        }
    }
}
