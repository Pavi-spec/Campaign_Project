package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Version1_ImageModel {

    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private String alt;

    @ValueMapValue
    private String linkURL;

    @ValueMapValue(name = "jcr:title")
    private String title;

    @ValueMapValue
    private boolean displayPopupTitle;

    @ValueMapValue
    private String id;

    @ValueMapValue
    private boolean isDecorative;


    public String getFileReference() {
        return fileReference;
    }

    public String getAlt() {
        return alt;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDisplayPopupTitle() {
        return displayPopupTitle;
    }

    public String getId() {
        return id;
    }

    public boolean isDecorative() {
        return isDecorative;
    }
}