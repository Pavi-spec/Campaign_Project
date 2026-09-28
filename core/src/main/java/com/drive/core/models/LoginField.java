package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class LoginField {

    @ValueMapValue
    private String fieldLabel;

    @ValueMapValue
    private String placeholder;

    @ValueMapValue
    private String fieldType;

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getFieldType() {
        return fieldType;
    }
}