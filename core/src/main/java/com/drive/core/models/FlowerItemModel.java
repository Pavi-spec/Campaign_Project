package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FlowerItemModel {

    @SlingObject
    private Resource resource;

    private String getProperty(String name) {
        if (resource == null) {
            return "";
        }

        ValueMap properties = resource.getValueMap();
        return properties.get(name, "");
    }

    public String getFlowerName() {
        return getProperty("flowerName");
    }

    public String getFlowerImage() {
        return getProperty("flowerImage");
    }

    public String getFlowerDescription() {
        return getProperty("flowerDescription");
    }

    public String getFlowerMeaning() {
        return getProperty("flowerMeaning");
    }

    public String getInitials() {
        return getProperty("initials");
    }
}