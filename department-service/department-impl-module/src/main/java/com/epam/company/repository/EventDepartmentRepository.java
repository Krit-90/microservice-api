package com.epam.company.repository;

import com.epam.company.entity.EventDepartment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventDepartmentRepository{
    @Insert("Insert Into department_monitoring(event, department_id, date_time) Values(#{event}, #{departmentId}," +
            " #{dateTime})")
    void save(EventDepartment event);
}
