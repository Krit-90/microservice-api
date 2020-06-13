package com.epam.company.repository;

import com.epam.company.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmployeeRepository {
    @Select("Select * From employees Where id = #{id}")
    @Results({
            @Result(property = "lastName",
                    column = "last_name"),
            @Result(property = "firstName",
                    column = "first_name"),
            @Result(property = "birthDate",
                    column = "birth_date"),
            @Result(property = "firedDate",
                    column = "fired_date"),
            @Result(property = "jobTitle",
                    column = "job_title"),
            @Result(property = "departmentId",
                    column = "department_id"),
            @Result(property = "isBoss",
                    column = "is_boss"),
            @Result(property = "employmentDate",
                    column = "employment_date"),
    })
    Optional<Employee> findById(@Param("id") Long id);

    @Select("Select * From employees Where last_name = #{lastName} And first_name = #{firstName}")
    @Results({
            @Result(property = "lastName",
                    column = "last_name"),
            @Result(property = "firstName",
                    column = "first_name"),
            @Result(property = "birthDate",
                    column = "birth_date"),
            @Result(property = "firedDate",
                    column = "fired_date"),
            @Result(property = "jobTitle",
                    column = "job_title"),
            @Result(property = "departmentId",
                    column = "department_id"),
            @Result(property = "isBoss",
                    column = "is_boss"),
            @Result(property = "employmentDate",
                    column = "employment_date"),
    })
    List<Employee> findByLastNameAndFirstName(@Param("lastName") String lastName, @Param("firstName") String firstName);

    @Select("Select * From employees Where department_id = #{departmentId}")
    @Results({
            @Result(property = "lastName",
                    column = "last_name"),
            @Result(property = "firstName",
                    column = "first_name"),
            @Result(property = "birthDate",
                    column = "birth_date"),
            @Result(property = "firedDate",
                    column = "fired_date"),
            @Result(property = "jobTitle",
                    column = "job_title"),
            @Result(property = "departmentId",
                    column = "department_id"),
            @Result(property = "isBoss",
                    column = "is_boss"),
            @Result(property = "employmentDate",
                    column = "employment_date"),
    })
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Select("Select count(department_id) From employees Where department_id = #{departmentId}")
    int countIdByDepartmentId(@Param("departmentId") Long departmentId);

    @Select("Select * From employees Where is_boss = true And department_id = #{departmentId}")
    @Results({
            @Result(property = "lastName",
                    column = "last_name"),
            @Result(property = "firstName",
                    column = "first_name"),
            @Result(property = "birthDate",
                    column = "birth_date"),
            @Result(property = "firedDate",
                    column = "fired_date"),
            @Result(property = "jobTitle",
                    column = "job_title"),
            @Result(property = "departmentId",
                    column = "department_id"),
            @Result(property = "isBoss",
                    column = "is_boss"),
            @Result(property = "employmentDate",
                    column = "employment_date"),
    })
    Employee getBossOfDepartment(@Param("departmentId") Long departmentId);

    @Select(value = "Select count(id) From employees Where is_boss = true And department_id = #{departmentId}")
    int countBossOfDepartment(@Param("departmentId") Long departmentId);

    @Delete("Delete * From employees Where id = #{id}")
    void deleteById(@Param("id") Long id);

    @Insert("Insert Into employees(last_name, first_name, patronymic, gender, birth_date, phone, email," +
            " employment_date, fired_date, job_title, salary, is_boss, department_id)" +
            "Values(#{lastName}, #{firstName}, #{patronymic}, #{gender}, #{birthDate}, #{phone}, #{email}," +
            " #{employmentDate}, #{firedDate}, #{jobTitle}, #{salary}, #{isBoss}, #{departmentId})")
    @SelectKey(statement = "Select lastval()", keyProperty = "id", before = false, resultType = Long.class)
    void save(Employee employee);

    @Update("Update employees Set last_name = #{lastName}, first_name = #{firstName}," +
            " patronymic = #{patronymic}, gender = #{gender}, birth_date = #{birthDate}, phone = #{phone}," +
            " email = #{email}, employment_date = #{employmentDate}, fired_date = #{firedDate}, job_title = #{jobTitle}," +
            " salary = #{salary}, is_boss = #{isBoss}, department_id = #{departmentId} Where id =#{id}")
    void update(Employee employee);
}
