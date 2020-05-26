package com.epam.company.repository;

import com.epam.company.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByLastNameAndFirstName(String lastName, String firstName);

    List<Employee> findByDepartmentId(Long id);

    int countIdByDepartmentId(Long departmentId);

    @Query(value = "Select b From Employee b Where b.isBoss = true and b.departmentId = :departmentId")
    Employee getBossOfDepartment(@Param("departmentId") Long id);

    @Query(value = "Select count(b.id) From Employee b Where b.isBoss = true and b.departmentId = :departmentId")
    int countBossOfDepartment(@Param("departmentId") Long id);
}
