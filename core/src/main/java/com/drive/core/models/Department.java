package com.drive.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Department {

    @ValueMapValue
    private String departmentName;

    @ValueMapValue
    private String departmentHead;

    @ValueMapValue
    private String departmentLocation;

    @ChildResource(name = "employees")
    private List<Employee> employees;

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public String getDepartmentLocation() {
        return departmentLocation;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}