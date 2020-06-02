package com.epam.company.service.impl;

import com.epam.company.dto.EmployeeDto;
import com.epam.company.dto.JobTitle;
import com.epam.company.dto.Sex;
import com.epam.company.entity.Employee;
import com.epam.company.exception.NoSuchElementInDBException;
import com.epam.company.repository.EmployeeRepository;
import com.epam.company.util.DepartmentDataCaller;
import com.epam.company.util.MapperEmployee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.validation.ValidationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

class EmployeeServiceImplTest {

    private static final BigDecimal SALARY_BOSS = BigDecimal.valueOf(30000);
    private static final long BOSS_ID = 1L;
    private static final long DEPARTMENT_ID = 1L;
    private static final long EMPLOYEE_ID = 3L;
    private static final LocalDate FIRED_DATE = LocalDate.of(2009, 10, 10);
    private static final LocalDate FIRED_DATE_BEFORE_EMPLOYEE_DATE = LocalDate.of(2008, 10, 10);
    private static final LocalDate EMPLOYEE_BIRTH_DATE = LocalDate.of(1975, 6, 21);
    private static final LocalDate EMPLOYEE_EMPLOYMENT_DATE = LocalDate.of(2008, 11, 25);
    private static final BigDecimal EMPLOYEE_SALARY = BigDecimal.valueOf(29000);
    private static final Integer COUNT_EMPLOYEES = Integer.valueOf(2);
    private static final String LAST_NAME = "Носков";
    private static final String FIRST_NAME = "Николай";
    private static final String EMPLOYEE_PHONE = "-7(900)900-77-77";
    private static final String EMPLOYEE_EMAIL = "bloblo@gmail.com";
    private static final String EMPLOYEE_PATRONYMIC = "Леонидович";
    private static final String BOSS_LAST_NAME = "Сидоров";
    private static final String BOSS_FIRST_NAME = "Петр";
    private static final String BOSS_PATRONYMIC = "Сидорович";
    private static final LocalDate BOSS_BIRTH_DATE = LocalDate.of(1987, 1, 1);
    private static final String BOSS_PHONE = "+7(900)900-99-99";
    private static final String BOSS_EMAIL = "bla@gmail.com";
    private static final LocalDate BOSS_EMPLOYMENT_DATE = LocalDate.of(2010, 4, 10);
    private static final Long NEW_DEPARTMENT_ID = 2L;
    public static final int COUNT_OF_BOSS = 1;
    private Employee boss;
    private EmployeeDto bossDto;
    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    MapperEmployee mapperEmployee;
    @Mock
    DepartmentDataCaller departmentDataCaller;
    @Mock
    Validator validator;
    @InjectMocks
    EmployeeServiceImpl employeeService;
    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testEmployee = new Employee(EMPLOYEE_ID, LAST_NAME, FIRST_NAME, EMPLOYEE_PATRONYMIC, Sex.MALE,
                EMPLOYEE_BIRTH_DATE, EMPLOYEE_PHONE, EMPLOYEE_EMAIL, EMPLOYEE_EMPLOYMENT_DATE,
                null, JobTitle.COMMON, EMPLOYEE_SALARY, false, DEPARTMENT_ID);

        testEmployeeDto = new EmployeeDto(LAST_NAME, FIRST_NAME, EMPLOYEE_PATRONYMIC, Sex.MALE, EMPLOYEE_BIRTH_DATE,
                EMPLOYEE_PHONE, EMPLOYEE_EMAIL, EMPLOYEE_EMPLOYMENT_DATE, null, JobTitle.COMMON,
                EMPLOYEE_SALARY, false, DEPARTMENT_ID);

        boss = new Employee(BOSS_ID, BOSS_LAST_NAME, BOSS_FIRST_NAME, BOSS_PATRONYMIC, Sex.MALE, BOSS_BIRTH_DATE,
                BOSS_PHONE, BOSS_EMAIL, BOSS_EMPLOYMENT_DATE, null, JobTitle.BOSS, SALARY_BOSS, true,
                DEPARTMENT_ID);

        bossDto = new EmployeeDto(BOSS_ID, BOSS_LAST_NAME, BOSS_FIRST_NAME, BOSS_PATRONYMIC, Sex.MALE, BOSS_BIRTH_DATE,
                BOSS_PHONE, BOSS_EMAIL, BOSS_EMPLOYMENT_DATE, null, JobTitle.BOSS, SALARY_BOSS,
                true);

        bossDto.setDepartmentId(DEPARTMENT_ID);

/*        Mockito.when(mapperEmployee.employeeToDto(boss)).thenReturn(bossDto);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);
        Mockito.when(employeeRepository.findById(BOSS_ID)).thenReturn(Optional.of(boss));
        Mockito.when(employeeRepository.countBossOfDepartment(DEPARTMENT_ID)).thenReturn(1);
        Mockito.when(employeeRepository.countIdByDepartmentId(DEPARTMENT_ID)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);*/
    }

    @Test
    public void addOrUpdateEmployeeWithoutId() {
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);
        employeeService.addOrUpdateEmployee(testEmployeeDto);
        verify(mapperEmployee).DtoToEmployee(testEmployeeDto);
        verify(employeeRepository).save(Mockito.any(Employee.class));
        verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void addOrUpdateEmployeeWithId() {
        testEmployeeDto.setId(testEmployee.getId());
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);

        employeeService.addOrUpdateEmployee(testEmployeeDto);

        verify(mapperEmployee).DtoToEmployee(testEmployeeDto);
        verify(employeeRepository).save(Mockito.any(Employee.class));
        verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void addOrUpdateEmployeeWhenEmployeeNotFound() {
        testEmployeeDto.setId(EMPLOYEE_ID);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Работник не найден"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidEmploymentDate() {
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);

        testEmployeeDto.setEmploymentDate(LocalDate.of(200, 1, 1));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Дата приема на работу, должна быть после дня рождения"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidFired() {
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);

        testEmployeeDto.setFiredDate(LocalDate.of(200, 1, 1));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().
                contains("При создании или обновлении не должно быть даты увольнения"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidCountBoss() {
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(employeeRepository.countBossOfDepartment(DEPARTMENT_ID)).thenReturn(COUNT_OF_BOSS);

        testEmployeeDto.setBoss(true);

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Может быть лишь один руководитель"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidSalary() {
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);

        testEmployeeDto.setSalary(SALARY_BOSS.add(BigDecimal.valueOf(5000)));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Зарплата не может быть больше, чем у руководителя"));
    }

    @Test
    public void firedEmployee() {
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        employeeService.firedEmployee(EMPLOYEE_ID, FIRED_DATE);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(FIRED_DATE, employeeCaptor.getValue().getFiredDate());
        Assertions.assertNull(employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void firedEmployeeInvalidFiredDate() {
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.firedEmployee(EMPLOYEE_ID, FIRED_DATE_BEFORE_EMPLOYEE_DATE));
        Assertions.assertTrue(exception.getMessage().contains("Дата увольнения, должна быть после приема на работу"));
    }


    @Test
    public void getEmployeeInfoById() {
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        employeeService.getEmployeeInfoById(EMPLOYEE_ID);
        Mockito.verify(employeeRepository).findById(EMPLOYEE_ID);
        Mockito.verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void changeDepartmentOfEmployee() {
        testEmployeeDto.setDepartmentId(NEW_DEPARTMENT_ID);

        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        Mockito.when(employeeRepository.getBossOfDepartment(NEW_DEPARTMENT_ID)).thenReturn(boss);
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(departmentDataCaller.isDepartmentExist(NEW_DEPARTMENT_ID)).thenReturn(true);

        employeeService.changeDepartmentOfEmployee(EMPLOYEE_ID, NEW_DEPARTMENT_ID);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(NEW_DEPARTMENT_ID, employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void changeDepartmentOfAllEmployeeFromSame() {
        List<Employee> testList = Collections.singletonList(testEmployee);
        testEmployeeDto.setDepartmentId(NEW_DEPARTMENT_ID);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        Mockito.when(employeeRepository.findByDepartmentId(testEmployee.getDepartmentId())).thenReturn(testList);
        Mockito.when(employeeRepository.getBossOfDepartment(NEW_DEPARTMENT_ID)).thenReturn(boss);
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
        Mockito.when(departmentDataCaller.isDepartmentExist(NEW_DEPARTMENT_ID)).thenReturn(true);
        employeeService.changeDepartmentOfAllEmployeeFromSame(testEmployee.getDepartmentId(), NEW_DEPARTMENT_ID);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(NEW_DEPARTMENT_ID, employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void getEmployeesOfDepartment() {
        List<Employee> testList = Collections.singletonList(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByDepartmentId(DEPARTMENT_ID)).thenReturn(testList);
        employeeService.getEmployeesOfDepartment(testEmployee.getDepartmentId());
        Mockito.verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void getBossOfEmployee() {
        Mockito.when(mapperEmployee.employeeToDto(boss)).thenReturn(bossDto);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        assertEquals(bossDto, employeeService.getBossOfEmployee(EMPLOYEE_ID));
    }

    @Test
    public void getBossOfEmployeeWithInvalidId() {
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> employeeService.getBossOfEmployee(boss.getId()));
        Assertions.assertTrue(exception.getMessage().contains("Работник не найден"));
    }

    @Test
    public void getBossOfDepartment() {
        Mockito.when(mapperEmployee.employeeToDto(boss)).thenReturn(bossDto);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);
        assertEquals(bossDto, employeeService.getBossOfDepartment(DEPARTMENT_ID));
    }

    @Test
    public void getEmployeesByLastNameAndFirstName() {
        List<Employee> testList = Collections.singletonList(testEmployee);
        List<EmployeeDto> testDtoList = Collections.singletonList(testEmployeeDto);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByLastNameAndFirstName(LAST_NAME, FIRST_NAME)).thenReturn(testList);
        assertEquals(testDtoList, employeeService.getEmployeesByLastNameAndFirstName(LAST_NAME, FIRST_NAME));
    }

    @Test
    public void getCountEmployeesOfDepartment() {
        Mockito.when(employeeRepository.countIdByDepartmentId(DEPARTMENT_ID)).thenReturn(COUNT_EMPLOYEES);
        assertEquals(COUNT_EMPLOYEES, employeeService.getCountEmployeesOfDepartment(DEPARTMENT_ID));
    }
}
