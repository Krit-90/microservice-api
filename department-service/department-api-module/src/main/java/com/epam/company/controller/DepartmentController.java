package com.epam.company.controller;

import com.epam.company.dto.DepartmentDto;
import com.epam.company.dto.DepartmentDtoReceive;
import com.epam.company.dto.EmployeeDto;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("departments")
@RestController
@Api(tags = "Departments API")
public interface DepartmentController {

    /**
     * Get-запрос по получению информации о департаменте
     *
     * @param id Id искомого департамента
     * @return Дто департамента, содержащий иформацию
     */
    @GetMapping("/{id}")
    DepartmentDto getDepartment(@PathVariable(name = "id") Long id);

    /**
     * Post-запрос по добавлению департамента, при создании необходимо указывать, в какой департамент входит,
     * исключение - главенствующий департамент
     *
     * @param departmentDtoReceive Дто добавляемого департамента с собственным названием и названием вышестоящего
     * @return Дто добавленного департамента либо сообщение о неуспешном завершении метода
     */
    @PostMapping("")
    DepartmentDtoReceive addDepartment(@RequestBody DepartmentDtoReceive departmentDtoReceive);

    /**
     * Put-запрос по смене названия у департамента
     *
     * @param newTitle Новое название
     * @param id       Id искомого департамента
     * @return Сущность добавленного департамента либо сообщение о неуспешном завершении метода
     */
    @PutMapping("/change-title/{id}")
    DepartmentDto changeTitle(@RequestParam(name = "new-title") String newTitle,
                              @PathVariable(name = "id") Long id);

    /**
     * Delete-запрос по удалению департамента
     *
     * @param id Id искомого департамента
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("")
    void removeDepartment(@RequestParam(name = "id") Long id);

    /**
     * Get-запрос по получению о всех вышестоящих департаментах
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов либо сообщение о неуспешном завершении метода
     */

    @GetMapping("/{id}/higher-department")
    List<DepartmentDto> getHigherDepartment(@PathVariable(name = "id") Long id);

    /**
     * Get-запрос по получению информации о ВСЕХ департаментах, находящихся в подчинении искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}/all-sub-department")
    List<DepartmentDto> getAllSubDepartment(@PathVariable(name = "id") Long id);

    /**
     * Get-запрос по получению информации о департаментах, находящихся в непосредственном подчинении искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}/sub-department")
    List<DepartmentDto> getSubDepartment(@PathVariable(name = "id") Long id);

    /**
     * Put-запрос о смене вышестоящего департамента
     *
     * @param idNewHead Id нового департамента
     * @param idCurrent Id искомого департамента
     * @return Измененный департамент либо сообщение о неуспешном завершении метода
     */
    @PutMapping("/change-head-department/{id}")
    DepartmentDto changeHeadDepartment(@RequestParam(name = "id-new-head") Long idNewHead,
                                       @PathVariable(name = "id") Long idCurrent);

    /**
     * Get-запрос по получению суммы зарплат работников искомого департамента
     *
     * @param id Id искомого департамента
     * @return Сумма зарплат либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}/sum-of-salary")
    BigDecimal getSumOfSalary(@PathVariable(name = "id") Long id);

    /**
     * Get-запрос по получению списка работников искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список дто работников либо сообщение о неуспешном завершении метода
     */
    @GetMapping("/{id}/employees")
    List<EmployeeDto> getEmployeesOfDepartment(@PathVariable(name = "id") Long id);

    /**
     * Существует ли данный департамент
     *
     * @param id Id искомого департамента
     * @return Логическое значение
     */
    @GetMapping("/{id}/is-exist")
    Boolean isExist(@PathVariable(name = "id") Long id);
}

