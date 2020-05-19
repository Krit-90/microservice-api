package com.epam.company.repository;

import com.epam.company.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByTitle(String title);
    Optional<Department> findById(Long id);
    int countByHeadDepartmentIsNull();

}
