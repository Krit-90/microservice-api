package com.epam.company.controller;

import com.epam.company.dto.EmployeeDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("employees")
@RestController
@Api(tags = "Employees API")
public interface EmployeeController {

    /**
     * Get-запрос по получению информации о сотруднике
     *
     * @param id Id искомого сотрудника
     * @return Дто искомого сотрудника либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}")
    EmployeeDto getEmployee(@PathVariable(name = "id") Long id);

    /**
     * Post-запрос по добавлению сотрудника
     *
     * @param employeeDto Данные о новом сотруднике
     * @return Дто добавленного сотрудника либо сообщение о неуспешном завершении метода
     */
    @PostMapping("")
    EmployeeDto addEmployee(@RequestBody EmployeeDto employeeDto);

    /**
     * Put-запрос по редактированию сведений о сотруднике департамента
     *
     * @param employeeDto Дто сотрудника из которого берем сведения
     * @return Данные отредактированного сотрудника либо сообщение о неуспешном завершении метода
     */
    @PutMapping("/{id}/update")
    EmployeeDto updateEmployee(@RequestBody EmployeeDto employeeDto);

    /**
     * Put-запрос по увольнению сотрудника
     *
     * @param id        Id искомого сотрудника
     * @param firedDate Дата увольнения
     * @return Уволенный сотрудник либо сообщение о неуспешном завершении метода
     */
    @PutMapping("/{id}/fired")
    EmployeeDto firedEmployee(@PathVariable(name = "id") Long id, @RequestBody LocalDate firedDate);

    /**
     * Put-запрос по переводу сотрудника из одного департамента в другой
     *
     * @param employeeId      Id искомого сотрудника
     * @param newDepartmentId Id нового департамента
     * @return Дто переведенного работника либо сообщение о неуспешном завершении метода
     */
    @PutMapping("/{id}/change-department")
    EmployeeDto changeDepartmentOfEmployee(@PathVariable(name = "id") Long employeeId,
                                           @RequestParam("new-department-id") Long newDepartmentId);

    /**
     * Put-запрос по переводу всех сотрудников департамента в другой департамент
     *
     * @param oldDepartmentId Id текущего департамент
     * @param newDepartmentId Id департамента, в который хотим перевести
     */
    @PutMapping("/change-department-employees")
    void changeDepartmentOfAllEmployeeFromSame(@RequestParam("old-department-id") Long oldDepartmentId,
                                               @RequestParam("new-department-id") Long newDepartmentId);

    /**
     * Get-запрос по получению информации о руководителе данного сотрудника
     *
     * @param id Id искомого сотрудника
     * @return Дто руководителя искомого сотрудника либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}/boss")
    EmployeeDto getBossOfEmployee(@PathVariable(name = "id") Long id);

    /**
     * Get-запрос по получению информации о руководителе данного департамента
     *
     * @param id Id искомого департамента
     * @return Дто руководителя искомого депаратамента либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{department-id}/department-boss")
    EmployeeDto getBossOfDepartment(@PathVariable(name = "department-id") Long id);

    /**
     * Get-запрос по получению списка всех сотрудников искомого департамента
     *
     * @param departmentId  Id искомого департамента
     * @return Список дто сотрудников либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{department-id}/employees")
    List<EmployeeDto> getEmployeesOfDepartment (@PathVariable("department-id") Long departmentId);

    /**
     * Get-запрос по количества всех сотрудников искомого департамента
     *
     * @param departmentId  Id искомого департамента
     * @return Количество сотрудников либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{department-id}/count-employees")
    Integer getCountEmployeesOfDepartment (@PathVariable("department-id") Long departmentId);

    /**
     * Get-запрос по получению списка сотрудников департамента по фамилии и имени
     *
     * @param lastName  Фамилия искомого сотрудника
     * @param firstName Имя искомого сотрудника
     * @return Список дто сотрудников с искомыми фамилией и именем либо сообщение о неуспешном завершении метода
     */
    @GetMapping("")
    List<EmployeeDto> getEmployeesByLastNameAndFirstName(@RequestParam("last-name") String lastName,
                                                         @RequestParam("first-name") String firstName);

}
