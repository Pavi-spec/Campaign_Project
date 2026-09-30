package com.drive.core.models.impl;

import com.drive.core.models.VersionTextModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = VersionTextModel.class,
        resourceType = "drive/components/version_text/version_text-2",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class VersionTextV2Model extends VersionTextV1Model {

    @ValueMapValue
    private String category;

    @ValueMapValue
    private String email;

    public String getCategory() {
        return category;
    }

    public String getEmail() {
        return email;
    }
}