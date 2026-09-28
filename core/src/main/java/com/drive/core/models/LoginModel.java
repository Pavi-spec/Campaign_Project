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
public class LoginModel {

    @ChildResource(name = "fields")
    private List<LoginField> fields;

    @ValueMapValue
    private String buttonText;

    public List<LoginField> getFields() {
        return fields;
    }

    public String getButtonText() {
        return buttonText != null
                ? buttonText
                : "Login";
    }
}