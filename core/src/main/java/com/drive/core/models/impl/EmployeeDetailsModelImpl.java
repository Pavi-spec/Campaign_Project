package com.drive.core.models.impl;

import com.drive.core.models.EmployeeDetailsModel;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = EmployeeDetailsModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EmployeeDetailsModelImpl implements EmployeeDetailsModel {

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

    @Override
    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String getEmployeeRole() {
        return employeeRole;
    }

    @Override
    public String getEmployeeEmail() {
        return employeeEmail;
    }

    @Override
    public boolean isEnableChapterDetails() {
        return enableChapterDetails;
    }

    @Override
    public String getChapterName() {
        return chapterName;
    }

    @Override
    public String getChapterNumber() {
        return chapterNumber;
    }

    @Override
    public String getChapterDescription() {
        return chapterDescription;
    }
}