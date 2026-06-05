package com.drive.core.models;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CampaignCardModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String offer;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String buttonText;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOffer() {
        return offer;
    }


    public String getImage() {
        return image;
    }

    public String getButtonText() {
        return buttonText;
    }
}