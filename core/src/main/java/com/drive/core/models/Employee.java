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
public class Employee {

    @ValueMapValue
    private String employeeName;

    @ValueMapValue
    private String email;

    @ValueMapValue
    private String phone;

    @ValueMapValue
    private String designation;

    @ValueMapValue
    private Integer experience;

    @ValueMapValue
    private String employeeImage;

    @ValueMapValue
    private String employeeLink;

    @ChildResource(name = "projects")
    private List<Project> projects;

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDesignation() {
        return designation;
    }

    public Integer getExperience() {
        return experience;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public String getEmployeeLink() {
        return employeeLink;
    }

    public List<Project> getProjects() {
        return projects;
    }
}