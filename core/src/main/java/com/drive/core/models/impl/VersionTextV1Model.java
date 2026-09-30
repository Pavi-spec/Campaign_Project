package com.drive.core.models.impl;

import com.drive.core.models.VersionTextModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = VersionTextModel.class,
        resourceType = "drive/components/version_text/version_text-1",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class VersionTextV1Model implements VersionTextModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String author;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAuthor() {
        return author;
    }
}