package com.drive.core.models.impl;

import com.drive.core.models.Heading;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = Heading.class,
        resourceType = HeadingImpl.RESOURCE_PATH,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeadingImpl implements Heading {

    public static final String RESOURCE_PATH =
            "Campaign_Project/ui.apps/src/main/content/jcr_root/apps/drive/components/heading";

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
