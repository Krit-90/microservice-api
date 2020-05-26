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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.validation.ValidationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

class EmployeeServiceImplTest {

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
    BigDecimal SALARY_BOSS = BigDecimal.valueOf(30000);
    Employee boss;
    EmployeeDto bossDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        boss = new Employee(1L, "Сидоров", "Петр", "Сидорович",
                Sex.MALE, LocalDate.of(1987, 1, 1),
                "+7(900)900-99-99", "bla@gmai.com",
                LocalDate.of(2010, 4, 10), null, JobTitle.BOSS,
                SALARY_BOSS, true, 1L);
        bossDto = new EmployeeDto(2L, "Петров", "Владимир", "Петрович",
                Sex.MALE, LocalDate.of(1988, 1, 1),
                "+7(900)900-88-88", "blabla@gmai.com",
                LocalDate.of(2012, 4, 17), null, JobTitle.COMMON,
                BigDecimal.valueOf(27000), false);
        bossDto.setDepartmentId(boss.getDepartmentId());

        Mockito.when(mapperEmployee.employeeToDto(boss)).thenReturn(bossDto);
        Mockito.when(employeeRepository.getBossOfDepartment(1L)).thenReturn(boss);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(boss));
        Mockito.when(employeeRepository.countBossOfDepartment(1L)).thenReturn(1);
        Mockito.when(employeeRepository.countIdByDepartmentId(boss.getDepartmentId())).thenReturn(3);
        Mockito.when(departmentDataCaller.isDepartmentExist(1L)).thenReturn(true);
    }

    @Test
    public void addOrUpdateEmployee() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.save(testEmployee())).thenReturn(testEmployee());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));

        assertEquals(testEmployeeDto, employeeService.addOrUpdateEmployee(testEmployeeDto));

        testEmployeeDto.setId(testEmployee.getId());
        assertEquals(testEmployeeDto, employeeService.addOrUpdateEmployee(testEmployeeDto));

        testEmployeeDto.setEmploymentDate(LocalDate.of(100, 1, 1));
        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Дата приема на работу, должна быть после дня рождения"));
        testEmployeeDto.setEmploymentDate(testEmployeeDto().getEmploymentDate());

        testEmployeeDto.setFiredDate(LocalDate.of(100, 1, 1));
        exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().
                contains("При создании или обновлении не должно быть даты увольнения"));
        testEmployeeDto.setFiredDate(testEmployeeDto().getFiredDate());

        testEmployeeDto.setBoss(true);
        exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Может быть лишь один руководитель"));
        testEmployeeDto.setBoss(false);

        testEmployeeDto.setSalary(SALARY_BOSS.add(BigDecimal.valueOf(5000)));
        exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.addOrUpdateEmployee(testEmployeeDto));
        Assertions.assertTrue(exception.getMessage().contains("Зарплата не может быть больше, чем у руководителя"));

    }

    @Test
    public void removeEmployee() {
        Employee testEmployee = testEmployee();
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));
    }

    @Test
    public void firedEmployee() {
        LocalDate firedDate = LocalDate.of(2009, 10, 10);
        LocalDate firedDateBeforeEmployeeDate = LocalDate.of(2008, 10, 10);
        Employee testEmployee = testEmployee();
        Mockito.when(employeeRepository.findById(3L)).thenReturn(Optional.of(testEmployee));

        employeeService.firedEmployee(3L, firedDate);
        assertEquals(firedDate, testEmployee.getFiredDate());

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.firedEmployee(3L, firedDateBeforeEmployeeDate));
        Assertions.assertTrue(exception.getMessage().contains("Дата увольнения, должна быть после приема на работу"));
    }


    @Test
    public void getEmployeeInfoById() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        Mockito.when(mapperEmployee.DtoToEmployee(testEmployeeDto)).thenReturn(testEmployee);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee()));
        assertEquals(testEmployeeDto, employeeService.getEmployeeInfoById(testEmployee.getId()));
    }

    @Test
    public void changeDepartmentOfEmployee() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();

        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        Mockito.when(departmentDataCaller.isDepartmentExist(2L)).thenReturn(true);

        employeeService.changeDepartmentOfEmployee(3L, 2L);
        Assertions.assertEquals(Long.valueOf(2L), testEmployee.getDepartmentId());
    }

    @Test
    public void changeDepartmentOfAllEmployeeFromSame() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        List<Employee> testList = Arrays.asList(testEmployee);
        Mockito.when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByDepartmentId(testEmployee.getDepartmentId())).thenReturn(testList);
        Mockito.when(departmentDataCaller.isDepartmentExist(3L)).thenReturn(true);
        employeeService.changeDepartmentOfAllEmployeeFromSame(testEmployee.getDepartmentId(), 3L);
        assertEquals(Long.valueOf(3L), testEmployee.getDepartmentId());
    }

    @Test
    public void getEmployeesOfDepartment() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        List<Employee> testList = Arrays.asList(testEmployee);
        List<EmployeeDto> testDtoList = Arrays.asList(testEmployeeDto);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByDepartmentId(testEmployee.getDepartmentId())).thenReturn(testList);
        assertEquals(testDtoList, employeeService.getEmployeesOfDepartment(testEmployee.getDepartmentId()));
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
        assertEquals(bossDto, employeeService.getBossOfDepartment(boss.getDepartmentId()));
    }

    @Test
    public void getEmployeesByLastNameAndFirstName() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();
        List<Employee> testList = Arrays.asList(testEmployee);
        List<EmployeeDto> testDtoList = Arrays.asList(testEmployeeDto);
        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(employeeRepository.findByLastNameAndFirstName(testEmployee.getLastName(),
                testEmployee.getFirstName())).thenReturn(testList);
        assertEquals(testDtoList, employeeService.getEmployeesByLastNameAndFirstName(testEmployee.getLastName(),
                testEmployee.getFirstName()));
    }

    @Test
    public void getCountEmployeesOfDepartment() {
        assertEquals(Integer.valueOf(3), employeeService.getCountEmployeesOfDepartment(boss.getDepartmentId()));
    }

    EmployeeDto testEmployeeDto() {
        return new EmployeeDto("Носков", "Николай", "Леонидович",
                Sex.MALE, LocalDate.of(1975, 6, 21),
                "-7(900)900-77-77", "bloblo@gmai.com",
                LocalDate.of(2008, 11, 25), null, JobTitle.COMMON,
                BigDecimal.valueOf(29000), false, 1L);
    }

    Employee testEmployee() {
        return new Employee(3L, "Носков", "Николай", "Леонидович",
                Sex.MALE, LocalDate.of(1975, 6, 21),
                "-7(900)900-77-77", "bloblo@gmai.com",
                LocalDate.of(2008, 11, 25), null, JobTitle.COMMON,
                BigDecimal.valueOf(29000), false, 1L);
    }


}