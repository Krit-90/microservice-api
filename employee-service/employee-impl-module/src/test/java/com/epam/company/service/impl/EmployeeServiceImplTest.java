package com.epam.company.service.impl;

import com.epam.company.dto.EmployeeDto;
import com.epam.company.dto.JobTitle;
import com.epam.company.dto.Sex;
import com.epam.company.entity.Employee;
import com.epam.company.repository.EmployeeRepository;
import com.epam.company.util.DepartmentDataCaller;
import com.epam.company.util.MapperEmployee;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

public class EmployeeServiceImplTest {
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
    Employee employee1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        boss = new Employee(1L, "Сидоров", "Петр", "Сидорович",
                Sex.MALE, LocalDate.of(1987, 1, 1),
                "+7(900)900-99-99", "bla@gmai.com",
                LocalDate.of(2010, 4, 10), null, JobTitle.BOSS,
                SALARY_BOSS, true, 1L);
        employee1 = new Employee(2L, "Петров", "Владимир", "Петрович",
                Sex.MALE, LocalDate.of(1988, 1, 1),
                "+7(900)900-88-88", "blabla@gmai.com",
                LocalDate.of(2012, 4, 17), null, JobTitle.COMMON,
                BigDecimal.valueOf(27000), false, 1L);

        Mockito.when(employeeRepository.getBossOfDepartment(1L)).thenReturn(boss);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(boss));
        Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee1));
        Mockito.when(employeeRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(boss, employee1));
        Mockito.when(employeeRepository.countBossOfDepartment(1L)).thenReturn(1);
        Mockito.when(employeeRepository.countIdByDepartmentId(1L)).thenReturn(3);
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

        assertEquals(testEmployeeDto, employeeService.addOrUpdateEmployee(testEmployeeDto));

        testEmployeeDto.setEmploymentDate(LocalDate.of(100, 1, 1));
 /*       Exception exception = Assertions.assertThrows(ValidationException.class,
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
*/

    }

    @Test
    public void removeEmployee() {

    }

    @Test
    public void firedEmployee() {
        LocalDate firedDate = LocalDate.of(2009, 10, 10);
        LocalDate firedDateBeforeEmployeeDate = LocalDate.of(2008, 10, 10);
        Employee testEmployee = testEmployee();
        Mockito.when(employeeRepository.findById(3L)).thenReturn(Optional.of(testEmployee));

        employeeService.firedEmployee(3L, firedDate);
        assertEquals(firedDate, testEmployee.getFiredDate());

/*        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> employeeService.firedEmployee(3L, firedDateBeforeEmployeeDate));
        Assertions.assertTrue(exception.getMessage().contains("Дата увольнения, должна быть после приема на работу"));*/
    }

    //    TODO: нужно ли это проверять, ведь действия в этом методе уже есть в проверенных

    @Test
    public void getEmployeeInfoById() {
    }

    @Test
    public void changeDepartmentOfEmployee() {
        EmployeeDto testEmployeeDto = testEmployeeDto();
        Employee testEmployee = testEmployee();

        Mockito.when(mapperEmployee.employeeToDto(testEmployee)).thenReturn(testEmployeeDto);
        Mockito.when(validator.validate(testEmployeeDto)).thenReturn(new HashSet<>());
        Mockito.when(employeeRepository.findById(3L)).thenReturn(Optional.of(testEmployee));
        Mockito.when(departmentDataCaller.isDepartmentExist(2L)).thenReturn(true);

        employeeService.changeDepartmentOfEmployee(3L, 2L);
//        Assertions.assertEquals(Long.valueOf(2L), testEmployee.getDepartmentId());
    }

    @Test
    public void changeDepartmentOfAllEmployeeFromSame() {
    }

    @Test
    public void getEmployeesOfDepartment() {
    }

    @Test
    public void getEmployeesByLastNameAndFirstName() {
    }

    @Test
    public void getCountEmployeesOfDepartment() {
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