package com.epam.company.repository;

import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface DepartmentSnapshotRepository {
    @Select("Select exist From department_snapshot Where department_id = #{departmentId}")
    Optional<Boolean> isExist(@Param("departmentId") Long departmentId);

    @Insert("Insert Into department_snapshot(department_id, exist) Values(#{departmentId}, #{exist})")
    void save(@Param("departmentId")Long departmentId, @Param("exist") boolean exist);

    @Update("Update department_snapshot Set exist = #{exist} Where department_id = #{departmentId}")
    void update(@Param("departmentId")Long departmentId, @Param("exist") boolean exist);
}
