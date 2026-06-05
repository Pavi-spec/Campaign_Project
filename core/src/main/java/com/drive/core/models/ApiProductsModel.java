package com.drive.core.models;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ApiProductsModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String buttonText;

    public String getHeading() {
        return heading;
    }

    public String getButtonText() {
        return buttonText;
    }
}