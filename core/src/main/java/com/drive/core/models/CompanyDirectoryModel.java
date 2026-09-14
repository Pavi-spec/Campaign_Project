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
public class CompanyDirectoryModel {

    @ValueMapValue
    private String companyName;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String location;

    @ChildResource(name = "departments")
    private List<Department> departments;

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public List<Department> getDepartments() {
        return departments;
    }
}