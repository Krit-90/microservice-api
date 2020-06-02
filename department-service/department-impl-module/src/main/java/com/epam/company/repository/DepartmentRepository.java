package com.epam.company.repository;

import com.epam.company.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByTitle(String title);
    int countByHeadDepartmentIsNull();
    @Modifying
    @Query(value = "Insert Into department_monitoring(event, department_id, date_time)  Values(:event," +
            " :departmentId, Now())", nativeQuery = true)
    void addEvent(@Param("event") String event, @Param("departmentId") Long departmentId);
    @Modifying
    @Query(value = "Insert Into departments_fund(sum_salaries, department_id)  Values(:sumSalaries," +
            " :departmentId)", nativeQuery = true)
    void addSumSalaryOfDepartment(@Param("sumSalaries") BigDecimal sumSalaries,
                                  @Param("departmentId") Long departmentId);
}
