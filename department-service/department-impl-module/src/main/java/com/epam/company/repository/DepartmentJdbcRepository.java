package com.epam.company.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentJdbcRepository {
    void dumpLoad();
}
