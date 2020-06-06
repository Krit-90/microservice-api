package com.epam.company.repository;

import com.epam.company.entity.EventDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDepartmentRepository extends JpaRepository<EventDepartment, Long> {
}
