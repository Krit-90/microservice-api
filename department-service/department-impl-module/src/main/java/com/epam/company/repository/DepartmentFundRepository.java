package com.epam.company.repository;

import com.epam.company.entity.DepartmentFund;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentFundRepository{
    @Delete("Delete From departments_fund")
    void deleteAll();
    @Insert("Insert Into departments_fund(sum_salaries, department_id) Values(#{sumSalaries}, #{departmentId})" +
            " returning id")
    Long save(DepartmentFund fund);
}
