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
import java.util.*;

import static org.junit.Assert.assertEquals;

class EmployeeServiceImplTest {

    public static final BigDecimal SALARY_BOSS = BigDecimal.valueOf(30000);
    public static final long BOSS_ID = 1L;
    public static final long DEPARTMENT_ID = 1L;
    public static final long EMPLOYEE_ID = 3L;
    public static final LocalDate FIRED_DATE = LocalDate.of(2009, 10, 10);
    public static final LocalDate FIRED_DATE_BEFORE_EMPLOYEE_DATE = LocalDate.of(2008, 10, 10);
    public static final LocalDate EMPLOYEE_BIRTH_DATE = LocalDate.of(1975, 6, 21);
    public static final LocalDate EMPLOYEE_EMPLOYMENT_DATE = LocalDate.of(2008, 11, 25);
    public static final BigDecimal EMPLOYEE_SALARY = BigDecimal.valueOf(29000);
    public static final Integer COUNT_EMPLOYEES = Integer.valueOf(2);
    public static final String LAST_NAME = "Носков";
    public static final String FIRST_NAME = "Николай";
    public Employee boss;
    public EmployeeDto bossDto;
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
        boss = new Employee(BOSS_ID, "Сидоров", "Петр", "Сидорович",
                Sex.MALE, LocalDate.of(1987, 1, 1),
                "+7(900)900-99-99", "bla@gmail.com",
                LocalDate.of(2010, 4, 10), null, JobTitle.BOSS,
                SALARY_BOSS, true, DEPARTMENT_ID);
        bossDto = new EmployeeDto(BOSS_ID, "Сидоров", "Петр", "Сидорович",
                Sex.MALE, LocalDate.of(1987, 1, 1),
                "+7(900)900-99-99", "bla@gmail.com",
                LocalDate.of(2010, 4, 10), null, JobTitle.BOSS,
                SALARY_BOSS, true);
        bossDto.setDepartmentId(DEPARTMENT_ID);

        Mockito.when(mapperEmployee.employeeToDto(boss)).thenReturn(bossDto);
        Mockito.when(employeeRepository.getBossOfDepartment(DEPARTMENT_ID)).thenReturn(boss);
        Mockito.when(employeeRepository.findById(BOSS_ID)).thenReturn(Optional.of(boss));
        Mockito.when(employeeRepository.countBossOfDepartment(DEPARTMENT_ID)).thenReturn(1);
        Mockito.when(employeeRepository.countIdByDepartmentId(DEPARTMENT_ID)).thenReturn(COUNT_EMPLOYEES);
        Mockito.when(departmentDataCaller.isDepartmentExist(DEPARTMENT_ID)).thenReturn(true);
    }

    @Test
    public void addOrUpdateEmployeeWithoutId() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();

        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        assertEquals(testEmployeeDto, employeeService.addOrUpdateEmployee(testEmployeeDto));
    }

    @Test
    public void addOrUpdateEmployeeWithId() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();

        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        testEmployeeDto.setId(testEmployee.getId());
        assertEquals(testEmployeeDto, employeeService.addOrUpdateEmployee(testEmployeeDto));
    }

    @Test
    public void addOrUpdateEmployeeInvalidEmploymentDate() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        testEmployeeDto.setEmploymentDate(LocalDate.of(200, 1, 1));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Дата приема на работу, должна быть после дня рождения"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidFired() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        testEmployeeDto.setFiredDate(LocalDate.of(200, 1, 1));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().
                contains("При создании или обновлении не должно быть даты увольнения"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidCountBoss() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        testEmployeeDto.setBoss(true);
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Может быть лишь один руководитель"));
    }

    @Test
    public void addOrUpdateEmployeeInvalidSalary() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        testEmployeeDto.setSalary(SALARY_BOSS.add(BigDecimal.valueOf(5000)));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Зарплата не может быть больше, чем у руководителя"));
    }

    @Test
    public void firedEmployee() {
        Employee testEmployee = testEmployee();
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        employeeService.firedEmployee(EMPLOYEE_ID, FIRED_DATE);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(FIRED_DATE, employeeCaptor.getValue().getFiredDate());
        Assertions.assertNull(employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void firedEmployeeInvalidFiredDate() {
        Employee testEmployee = testEmployee();
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.firedEmployee(EMPLOYEE_ID, FIRED_DATE_BEFORE_EMPLOYEE_DATE));
        Assertions.assertTrue(exception.getMessage().contains("Дата увольнения, должна быть после приема на работу"));
    }


    @Test
    public void getEmployeeInfoById() {
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee()));
        employeeService.getEmployeeInfoById(EMPLOYEE_ID);
        Mockito.verify(employeeRepository).findById(EMPLOYEE_ID);
        Mockito.verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void changeDepartmentOfEmployee() {
        Long newDepartmentId = 2L;
        Employee testEmployee = testEmployee();

        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto());
        Mockito.when(validator.validate(testEmployeeDto())).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        Mockito.when(departmentDataCaller.isDepartmentExist(newDepartmentId)).thenReturn(true);

        employeeService.changeDepartmentOfEmployee(EMPLOYEE_ID, newDepartmentId);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(newDepartmentId, employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void changeDepartmentOfAllEmployeeFromSame() {
        Long newDepartmentId = 2L;
        Employee testEmployee = testEmployee();
        List<Employee> testList = Collections.singletonList(testEmployee);
        Mockito.when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto());
        Mockito.when(employeeRepository.findByDepartmentId(testEmployee.getDepartmentId())).thenReturn(testList);
        Mockito.when(departmentDataCaller.isDepartmentExist(newDepartmentId)).thenReturn(true);

        employeeService.changeDepartmentOfAllEmployeeFromSame(testEmployee.getDepartmentId(), newDepartmentId);
        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        assertEquals(newDepartmentId, employeeCaptor.getValue().getDepartmentId());
    }

    @Test
    public void getEmployeesOfDepartment() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        List<Employee> testList = Collections.singletonList(testEmployee);
        List<EmployeeDto> testDtoList = Collections.singletonList(testEmployeeDto);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByDepartmentId(DEPARTMENT_ID)).thenReturn(testList);
        employeeService.getEmployeesOfDepartment(testEmployee.getDepartmentId());
        Mockito.verify(mapperEmployee).employeeToDto(Mockito.any(Employee.class));
    }

    @Test
    public void getBossOfEmployee() {
        assertEquals(bossDto, employeeService.getBossOfEmployee(boss.getId()));
        Mockito.when(employeeRepository.findById(boss.getId())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(NoSuchElementInDBException.class,
                () -> employeeService.getBossOfEmployee(boss.getId()));
        Assertions.assertTrue(exception.getMessage().contains("Работник не найден"));
    }

    @Test
    public void getBossOfDepartment() {
        assertEquals(bossDto, employeeService.getBossOfDepartment(DEPARTMENT_ID));
    }

    @Test
    public void getEmployeesByLastNameAndFirstName() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        List<Employee> testList = Collections.singletonList(testEmployee);
        List<EmployeeDto> testDtoList = Collections.singletonList(testEmployeeDto);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByLastNameAndFirstName(LAST_NAME, FIRST_NAME)).thenReturn(testList);
        assertEquals(testDtoList, employeeService.getEmployeesByLastNameAndFirstName(LAST_NAME, FIRST_NAME));
    }

    @Test
    public void getCountEmployeesOfDepartment() {
        assertEquals(COUNT_EMPLOYEES, employeeService.getCountEmployeesOfDepartment(DEPARTMENT_ID));
    }

    EmployeeDto testEmployeeDto() {
        return new EmployeeDto(LAST_NAME, FIRST_NAME, "Леонидович",
                Sex.MALE, EMPLOYEE_BIRTH_DATE,
                "-7(900)900-77-77", "bloblo@gmail.com",
                EMPLOYEE_EMPLOYMENT_DATE, null, JobTitle.COMMON,
                EMPLOYEE_SALARY, false, DEPARTMENT_ID);
    }

    Employee testEmployee() {
        return new Employee(EMPLOYEE_ID, LAST_NAME, FIRST_NAME, "Леонидович",
                Sex.MALE, EMPLOYEE_BIRTH_DATE,
                "-7(900)900-77-77", "bloblo@gmail.com",
                EMPLOYEE_EMPLOYMENT_DATE, null, JobTitle.COMMON,
                EMPLOYEE_SALARY, false, DEPARTMENT_ID);
    }

}
