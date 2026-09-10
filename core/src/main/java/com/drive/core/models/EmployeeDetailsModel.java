package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EmployeeDetailsModel {

    @ValueMapValue
    private String employeeName;

    @ValueMapValue
    private String employeeRole;

    @ValueMapValue
    private String employeeEmail;

    @ValueMapValue
    private boolean enableChapterDetails;

    @ValueMapValue
    private String chapterName;

    @ValueMapValue
    private String chapterNumber;

    @ValueMapValue
    private String chapterDescription;

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public boolean isEnableChapterDetails() {
        return enableChapterDetails;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public String getChapterDescription() {
        return chapterDescription;
    }
}