package com.epam.company.repository;

import com.epam.company.entity.DepartmentFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentFundRepository extends JpaRepository<DepartmentFund, Long> {
}
