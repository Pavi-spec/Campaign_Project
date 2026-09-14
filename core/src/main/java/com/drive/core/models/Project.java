package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Project {

    @ValueMapValue
    private String projectName;

    @ValueMapValue
    private String projectDescription;

    @ValueMapValue
    private String projectUrl;

    @ValueMapValue
    private String projectImage;

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public String getProjectImage() {
        return projectImage;
    }
}