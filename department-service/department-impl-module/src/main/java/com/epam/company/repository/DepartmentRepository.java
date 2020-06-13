package com.epam.company.repository;

import com.epam.company.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface DepartmentRepository {
    @Select("Select * From departments Where title = #{title}")
    @Results({
            @Result(property = "id",
                    column = "id"),
            @Result(property = "creationDate",
                    column = "creation_date"),
            @Result(property = "headDepartment",
                    column = "head_department_id",
                    javaType = Department.class,
                    one = @One(select = "findById")),
            @Result(property = "subDepartment",
                    column = "id",
                    javaType = Set.class,
                    many = @Many(select = "findByHeadDepartmentId"))
    })
    Optional<Department> findByTitle(@Param("title") String title);

    @Select("Select * From departments")
    @Results({
            @Result(property = "id",
                    column = "id"),
            @Result(property = "creationDate",
                    column = "creation_date"),
            @Result(property = "headDepartment",
                    column = "head_department_id",
                    javaType = Department.class,
                    one = @One(select = "findById")),
            @Result(property = "subDepartment",
                    column = "id",
                    javaType = Set.class,
                    many = @Many(select = "findByHeadDepartmentId"))
    })
    List<Department> findAll();

    @Select("Select * From departments Where id = #{id}")
    @Results({
            @Result(property = "id",
                    column = "id"),
            @Result(property = "creationDate",
                    column = "creation_date"),
            @Result(property = "headDepartment",
                    column = "head_department_id",
                    javaType = Department.class,
                    one = @One(select = "findById")),
            @Result(property = "subDepartment",
                    column = "id",
                    javaType = Set.class,
                    many = @Many(select = "findByHeadDepartmentId"))
    })
    Optional<Department> findById(@Param("id") Long id);

//   TODO: Когда вытаскиваю из бд, делаю привязку только к субдепартаментам а к хеад - нет, а то будет StackOverFlow
    @Select("Select * From departments Where head_department_id = #{headDepartmentId}")
    @Results({
            @Result(property = "id",
                    column = "id"),
            @Result(property = "creationDate",
                    column = "creation_date"),
            @Result(property = "subDepartment",
                    column = "id",
                    javaType = Set.class,
                    many = @Many(select = "findByHeadDepartmentId"))
    })
    List<Department> findByHeadDepartmentId(@Param("headDepartmentId") Long headDepartmentId);

    @Delete("Delete * From departments Where id = #{id}")
    void deleteById(@Param("id") Long id);

    @Select("Select count(id) From departments Where head_department_id isNull")
    int countByHeadDepartmentIsNull();

    @Insert("Insert Into departments(title, creation_date, head_department_id) Values(#{title}, #{creationDate}," +
            " #{headDepartment.id})")
    @SelectKey(statement = "Select lastval()", keyProperty = "id", before = false, resultType = Long.class)
    void save(Department department);

    @Update("Update departments Set title = #{title}, creation_date = #{creationDate}," +
            " head_department_id = #{headDepartment.id}) Where id = #{id})")
    void update(Department department);
}
