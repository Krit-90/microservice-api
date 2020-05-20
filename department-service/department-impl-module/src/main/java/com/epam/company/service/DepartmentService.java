package com.epam.company.service;


import com.epam.company.dto.DepartmentDto;
import com.epam.company.dto.DepartmentDtoReceive;
import com.epam.company.dto.EmployeeDto;

import java.math.BigDecimal;
import java.util.List;

public interface DepartmentService {
    /**
     * Добавление департамента, при создании необходимо указывать, в какой департамент входит,
     * исключение - главенствующий департамент
     *
     * @param departmentDtoReceive Дто добавляемого департамента с собственным названием и названием вышестоящего
     * @return Дто добавленного департамента
     */
    DepartmentDtoReceive addDepartment(DepartmentDtoReceive departmentDtoReceive);

    /**
     * Обновление названия департамента
     *
     * @param newTitle Старое название
     * @param id       Id искомого департамента
     * @return Дто искомого департамента
     */
    DepartmentDto updateDepartmentTitle(String newTitle, Long id);

    /**
     * Удаление департамента
     *
     * @param id Id искомого департамента
     */
    void removeDepartment(Long id);

    /**
     * Получение информации о департаменте
     *
     * @param id Id искомого департамента
     * @return Дто департамента, содержащий иформацию
     */
    DepartmentDto getDepartmentInfoById(Long id);

    /**
     * Получение информации о департаментах, находящихся в непосредственном подчинении искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов
     */
    List<DepartmentDto> getSubordinateDepartments(Long id);

    /**
     * Получение информации о ВСЕХ департаментах, находящихся в подчинении искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов
     */
    List<DepartmentDto> getAllSubordinateDepartments(Long id);

    /**
     * Смена вышестоящего департамента
     *
     * @param idNewHead Id нового департамента
     * @param idCurrent Id искомого департамента
     * @return Дто искомого департамента
     */

    DepartmentDto changeHeadDepartment(Long idNewHead, Long idCurrent);

    /**
     * Получение информации о всех вышестоящих департаментах
     *
     * @param id Id искомого департамента
     * @return Список дто департаментов
     */
    List<DepartmentDto> getAllHigherDepartments(Long id);

    /**
     * Получение департамента по названию
     *
     * @param title Название искомого департамента
     * @return Дто искомого департамента
     */
    DepartmentDto getDepartmentByTitle(String title);

    /**
     * Получение суммы зарплат работников искомого департамента
     *
     * @param id Id искомого департамента
     * @return Сумма зарплат
     */
    BigDecimal getSumOfSalary(Long id);

    /**
     * Получение списка работников искомого департамента
     *
     * @param id Id искомого департамента
     * @return Список работников
     */
    List<EmployeeDto> getEmployeesOfDepartment(Long id);

    /**
     * Существует ли данный департамент
     *
     * @param id Id искомого департамента
     * @return Логическое значение
     */

    Boolean isExist(Long id);

}
