package com.epam.company.service;

import com.epam.company.dto.EmployeeDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    /**
     * Добавление или редактирование сотрудника
     *
     * @param employeeDto Данные о новом сотруднике или измененном
     * @return Дто добавленного или отредактированного сотрудника
     */
    EmployeeDto addOrUpdateEmployee(EmployeeDto employeeDto);

    /**
     * Удаление из базы
     *
     * @param id Id искомго сотрудкника
     */
    void removeEmployee(Long id);

    /**
     * Увольнение сотрудника
     *
     * @param id        Id искомого сотрудника
     * @param firedDate Дата увольнения
     * @return Уволенный сотрудник
     */
    EmployeeDto firedEmployee(Long id, LocalDate firedDate);

    /**
     * Получение информации о сотруднике
     *
     * @param id Id искомого сотрудника
     * @return Дто искомого сотрудника
     */
    EmployeeDto getEmployeeInfoById(Long id);

    /**
     * Перевод сотрудника из одного департамента в другой
     *
     * @param employeeId      Id искомого сотрудника
     * @param newDepartmentId Id нового департамента
     * @return Дто переведенного работника
     */
    EmployeeDto changeDepartmentOfEmployee(Long employeeId, Long newDepartmentId);

    /**
     * Перевод всех сотрудников департамента в другой департамент
     *
     * @param oldDepartmentId Id текущего департамент
     * @param newDepartmentId Id департамента, в который хотим перевести
     */
    void changeDepartmentOfAllEmployeeFromSame(Long oldDepartmentId, Long newDepartmentId);

    /**
     * Получение информации о руководителе данного сотрудника
     *
     * @param id Id искомого сотрудника
     * @return Дто руководителя искомого сотрудника
     */
    EmployeeDto getBossOfEmployee(Long id);

    /**
     * Получение информации о руководителе данного департамента
     *
     * @param id Id искомого департамента
     * @return Дто руководителя искомого сотрудника
     */
    EmployeeDto getBossOfDepartment(Long id);

    /**
     * Получение списка сотрудников департамента по фамилии и имени
     *
     * @param lastName  Фамилия искомого сотрудника
     * @param firstName Имя искомого сотрудника
     * @return Список дто сотрудников с искомыми фамилией и именем
     */
    List<EmployeeDto> getEmployeesByLastNameAndFirstName(String lastName, String firstName);


    /**
     * Получение информации о всех сотрудниках данного департамента
     *
     * @param id Id искомого департамента
     * @return Список дто сотрудников
     */
    List<EmployeeDto> getEmployeesOfDepartment(Long id);

    /**
     * Получение количества всех сотрудников искомого департамента
     *
     * @param id Id искомого департамента
     * @return количество сотрудников в депараменте
     */
    Integer getCountEmployeesOfDepartment(Long id);
}
