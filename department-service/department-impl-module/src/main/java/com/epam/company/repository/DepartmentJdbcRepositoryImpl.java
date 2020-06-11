package com.epam.company.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Repository
public class DepartmentJdbcRepositoryImpl implements DepartmentJdbcRepository {

    public static final String INSERT_DUMP = "Insert Into departments (title, creation_date, head_department_id)" +
            " Values (?, ?, ?)";
    public static final String DUMP_LOCATION = "department-service/department-impl-module/src/main/resources/dump.txt";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DepartmentJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void dumpLoad() {
            String query = INSERT_DUMP;
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(DUMP_LOCATION))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split("/");
                    jdbcTemplate.update(query, row[0], LocalDate.parse(row[1]), Long.valueOf(row[2]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
