package com.epam.company.service.impl;

import com.epam.company.dto.*;
import com.epam.company.entity.Department;
import com.epam.company.exception.NoSuchElementInDBException;
import com.epam.company.repository.DepartmentRepository;
import com.epam.company.util.CustomSpringEventPublisher;
import com.epam.company.util.EmployeeDataCaller;
import com.epam.company.util.MapperDepartment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentServiceImplTest {
    public static final long DEPARTMENT_ID = 1L;
    public static final String TITLE = "Test office";
    public static final LocalDate DEPARTMENT_CREATION_DATE = LocalDate.of(2000, 1, 1);
    public static final int COUNT_EMPLOYEES = 2;
    public static final Long HEAD_DEPARTMENT_ID = 2L;
    public static final LocalDate HEAD_DEPARTMENT_CREATION_DATE = LocalDate.of(1993, 2, 3);
    public static final String NEW_TITLE = "Новое название";
    public static final String EMPLOYEE_DTO_LAST_NAME = "Сидоров";
    public static final String EMPLOYEE_DTO_FIRST_NAME = "Петр";
    public static final String EMPLOYEE_DTO_PATRONYMIC = "Сидорович";
    public static final LocalDate EMPLOYEE_DTO_BIRTH_DATE = LocalDate.of(1987, 1, 1);
    public static final String EMPLOYEE_DTO_PHONE = "+7(900)900-99-99";
    public static final String EMPLOYEE_DTO_EMAIL = "bla@gmail.com";
    public static final LocalDate EMPLOYEE_DTO_EMPLOYMENT_DATE = LocalDate.of(2010, 4, 10);
    public static final BigDecimal EMPLOYEE_DTO_SALARY = BigDecimal.valueOf(30000);
    public static final String HEAD_DEPARTMENT_TITLE = "Test main office";
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    MapperDepartment mapperDepartment;
    @Mock
    EmployeeDataCaller employeeDataCaller;
    @Mock
    CustomSpringEventPublisher customSpringEventPublisher;
    @InjectMocks
    DepartmentServiceImpl departmentService;
    @Captor
    private ArgumentCaptor<Department> departmentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addDepartment() {
        DepartmentDtoReceive testDtoReceive = testDepartmentDtoReceive(HEAD_DEPARTMENT_ID);
        Mockito.when(departmentRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        Mockito.when(departmentRepository.countByHeadDepartmentIsNull()).thenReturn(1);
        Mockito.when(mapperDepartment.DtoReceiveToDepartment(testDtoReceive))
                .thenReturn(testDepartment(testHeadDepartment()));
        departmentService.addDepartment(testDtoReceive);
        Mockito.verify(departmentRepository).save(departmentCaptor.capture());
        assertEquals(HEAD_DEPARTMENT_ID, departmentCaptor.getValue().getHeadDepartment().getId());
    }

    @Test
    void addDepartmentWithDuplicateTitle() {
        Mockito.when(departmentRepository.findByTitle(TITLE)).
                thenReturn(Optional.of(testDepartment(testHeadDepartment())));
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        Mockito.when(departmentRepository.countByHeadDepartmentIsNull()).thenReturn(1);
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> departmentService.addDepartment(testDepartmentDtoReceive(HEAD_DEPARTMENT_ID)));
        Assertions.assertTrue(exception.getMessage().contains("Департамент с данным названием уже существует"));
    }

    @Test
    void addDepartmentWithDoubleMainOffice() {
        Mockito.when(departmentRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        Mockito.when(departmentRepository.countByHeadDepartmentIsNull()).thenReturn(1);
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.addDepartment(testDepartmentDtoReceive(null)));
        Assertions.assertTrue(exception.getMessage().contains("Самый верхний депаратмент уже существует"));
    }

    @Test
    void addDepartmentWithNotFoundHeadDepartment() {
        Mockito.when(departmentRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.countByHeadDepartmentIsNull()).thenReturn(1);
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.addDepartment(testDepartmentDtoReceive(HEAD_DEPARTMENT_ID)));
        Assertions.assertTrue(exception.getMessage().contains("Вышестоящий департамент не найден"));
    }

    @Test
    void updateDepartmentTitle() {
        Department testDepartment = testDepartment(null);
        Mockito.when(mapperDepartment.departmentToDto(testDepartment)).thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment)).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Mockito.when(departmentRepository.findByTitle(NEW_TITLE)).thenReturn(Optional.empty());
        departmentService.updateDepartmentTitle(NEW_TITLE, DEPARTMENT_ID);
        Mockito.verify(departmentRepository).update(departmentCaptor.capture());
        assertEquals(NEW_TITLE, departmentCaptor.getValue().getTitle());
    }

    @Test
    void updateDepartmentTitleWithWrongId() {
        Department testDepartment = testDepartment(null);
        Mockito.when(mapperDepartment.departmentToDto(testDepartment)).thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment)).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.updateDepartmentTitle(NEW_TITLE, DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент не найден"));
    }

    @Test
    void updateDepartmentTitleWithDuplicateTitle() {
        Department testDepartment = testDepartment(null);
        Mockito.when(mapperDepartment.departmentToDto(testDepartment)).thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment)).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Mockito.when(departmentRepository.findByTitle(NEW_TITLE)).thenReturn(Optional.of(testDepartment(null)));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> departmentService.updateDepartmentTitle(NEW_TITLE, DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент с данным названием уже существует"));
    }

    @Test
    void removeDepartment() {
        Department testDepartment = testDepartment(null);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        departmentService.removeDepartment(DEPARTMENT_ID);
        Mockito.verify(departmentRepository).deleteById(DEPARTMENT_ID);
    }

    @Test
    void removeDepartmentWithEmployees() {
        Department testDepartment = testDepartment(null);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Mockito.when(employeeDataCaller.getEmployees(DEPARTMENT_ID)).thenReturn(Collections.singletonList(testBoss()));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> departmentService.removeDepartment(DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage()
                .contains("Невозможно удаление департамента при наличии работников"));
    }

    @Test
    void getDepartmentInfoById() {
        Department testDepartment = testDepartment(null);
        Mockito.when(employeeDataCaller.getBoss(testDepartment)).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(mapperDepartment.departmentToDto(testDepartment)).thenReturn(testDepartmentDto());
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        departmentService.getDepartmentInfoById(DEPARTMENT_ID);
        Mockito.verify(departmentRepository).findById(DEPARTMENT_ID);
        Mockito.verify(mapperDepartment).departmentToDto(Mockito.any(Department.class));
    }

    @Test
    void getDepartmentInfoByIdWithWrongId() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.getDepartmentInfoById(DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент не найден"));
    }

    @Test
    void changeHeadDepartment() {
        Department testDepartment = testDepartment(null);
        Mockito.when(mapperDepartment.departmentToDto(testDepartment)).thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment)).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        departmentService.changeHeadDepartment(HEAD_DEPARTMENT_ID, DEPARTMENT_ID);
        Mockito.verify(departmentRepository).update(departmentCaptor.capture());
        assertEquals(HEAD_DEPARTMENT_ID, departmentCaptor.getValue().getHeadDepartment().getId());
    }

    @Test
    void changeHeadDepartmentSameIdAndHeadId() {
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> departmentService.changeHeadDepartment(DEPARTMENT_ID, DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент не может быть главныи для самого себя"));
    }

    @Test
    void changeHeadDepartmentWithWrongId() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.changeHeadDepartment(DEPARTMENT_ID, HEAD_DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент не найден"));
    }

    @Test
    void changeHeadDepartmentWithWrongHeadId() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testHeadDepartment()));
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> departmentService.changeHeadDepartment(DEPARTMENT_ID, HEAD_DEPARTMENT_ID));
        Assertions.assertTrue(exception.getMessage().contains("Департамент не найден"));
    }

    @Test
    void getAllHigherDepartments() {
        Department testDepartment = testDepartment(testHeadDepartment());
        Mockito.when(mapperDepartment.departmentToDto(testDepartment.getHeadDepartment()))
                .thenReturn(testHeadDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment.getHeadDepartment())).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment));
        Assertions.assertEquals(testDepartment.getHeadDepartment().getTitle(),
                departmentService.getAllHigherDepartments(DEPARTMENT_ID).get(0).getTitle());
    }

    @Test
    void getAllSubordinateDepartments() {
        Department testDepartment = testDepartment(testHeadDepartment());
        Mockito.when(mapperDepartment.departmentToDto(testDepartment.getHeadDepartment()))
                .thenReturn(testHeadDepartmentDto());
        Mockito.when(mapperDepartment.departmentToDto(testDepartment))
                .thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment.getHeadDepartment())).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID))
                .thenReturn(Optional.of(testDepartment.getHeadDepartment()));
        Assertions.assertEquals(testDepartment.getTitle(),
                departmentService.getAllSubordinateDepartments(HEAD_DEPARTMENT_ID).get(0).getTitle());
    }

    @Test
    void getSubordinateDepartments() {
        Department testDepartment = testDepartment(testHeadDepartment());
        Mockito.when(mapperDepartment.departmentToDto(testDepartment.getHeadDepartment()))
                .thenReturn(testHeadDepartmentDto());
        Mockito.when(mapperDepartment.departmentToDto(testDepartment))
                .thenReturn(testDepartmentDto());
        Mockito.when(employeeDataCaller.getBoss(testDepartment.getHeadDepartment())).thenReturn(testBoss());
        Mockito.when(employeeDataCaller.getCountEmployees(testDepartment)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentRepository.findById(HEAD_DEPARTMENT_ID))
                .thenReturn(Optional.of(testDepartment.getHeadDepartment()));
        Assertions.assertEquals(testDepartment.getTitle(),
                departmentService.getSubordinateDepartments(HEAD_DEPARTMENT_ID).get(0).getTitle());
    }

    @Test
    void getSumOfSalary() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment(null)));
        Mockito.when(employeeDataCaller.getEmployees(DEPARTMENT_ID)).thenReturn(Collections.singletonList(testBoss()));
        assertEquals(testBoss().getSalary(), departmentService.getSumOfSalary(DEPARTMENT_ID));
        Mockito.verify(employeeDataCaller).getEmployees(DEPARTMENT_ID);
    }

    @Test
    void getEmployeesOfDepartment() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment(null)));
        Mockito.when(employeeDataCaller.getEmployees(DEPARTMENT_ID)).thenReturn(Collections.singletonList(testBoss()));
        departmentService.getEmployeesOfDepartment(DEPARTMENT_ID);
        Mockito.verify(employeeDataCaller).getEmployees(DEPARTMENT_ID);
    }

    @Test
    void isExist() {
        Mockito.when(departmentRepository.findById(DEPARTMENT_ID)).thenReturn(Optional.of(testDepartment(null)));
        departmentService.isExist(DEPARTMENT_ID);
        Mockito.verify(departmentRepository).findById(DEPARTMENT_ID);
    }

    Department testDepartment(Department headDepartment) {
        Department department = new Department(DEPARTMENT_ID, TITLE, DEPARTMENT_CREATION_DATE, headDepartment);
        if (headDepartment != null && headDepartment.getSubDepartment() == null) {
            headDepartment.setSubDepartment(new HashSet<>());
            headDepartment.getSubDepartment().add(department);
        } else if (headDepartment != null) {
            headDepartment.getSubDepartment().add(department);
        }
        return department;
    }

    Department testHeadDepartment() {
        return new Department(HEAD_DEPARTMENT_ID, HEAD_DEPARTMENT_TITLE,
                HEAD_DEPARTMENT_CREATION_DATE, null);
    }

    DepartmentDto testHeadDepartmentDto() {
        return new DepartmentDto(HEAD_DEPARTMENT_ID, HEAD_DEPARTMENT_TITLE,
                HEAD_DEPARTMENT_CREATION_DATE, null, null);
    }

    DepartmentDto testDepartmentDto() {
        return new DepartmentDto(DEPARTMENT_ID, TITLE, DEPARTMENT_CREATION_DATE, null, null);
    }

    DepartmentDtoReceive testDepartmentDtoReceive(Long headId) {
        return new DepartmentDtoReceive(TITLE, headId);
    }

    EmployeeDto testBoss() {
        return new EmployeeDto(1L, EMPLOYEE_DTO_LAST_NAME, EMPLOYEE_DTO_FIRST_NAME, EMPLOYEE_DTO_PATRONYMIC,
                Sex.MALE, EMPLOYEE_DTO_BIRTH_DATE,
                EMPLOYEE_DTO_PHONE, EMPLOYEE_DTO_EMAIL,
                EMPLOYEE_DTO_EMPLOYMENT_DATE, null, JobTitle.BOSS,
                EMPLOYEE_DTO_SALARY, true);
    }
}