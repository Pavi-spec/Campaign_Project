package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Version2_ImageModel {

    // Image Asset
    @ValueMapValue
    private String fileReference;

    // Alternative Text
    @ValueMapValue
    private String alt;

    // Link
    @ValueMapValue
    private String linkURL;

    // Caption
    @ValueMapValue(name = "jcr:title")
    private String title;

    // Display caption as pop-up
    @ValueMapValue
    private boolean displayPopupTitle;

    // HTML ID
    @ValueMapValue
    private String id;

    // Decorative image
    @ValueMapValue
    private boolean isDecorative;

    // V2 - DAM alternative text
    @ValueMapValue
    private boolean altValueFromDAM;

    // V2 - DAM caption
    @ValueMapValue
    private boolean titleValueFromDAM;

    // V2 - Dynamic Media
    @ValueMapValue
    private String dmPresetType;

    @ValueMapValue
    private String imagePreset;

    @ValueMapValue
    private String smartCropRendition;

    @ValueMapValue
    private String imageModifiers;


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

    public boolean isAltValueFromDAM() {
        return altValueFromDAM;
    }

    public boolean isTitleValueFromDAM() {
        return titleValueFromDAM;
    }

    public String getDmPresetType() {
        return dmPresetType;
    }

    public String getImagePreset() {
        return imagePreset;
    }

    public String getSmartCropRendition() {
        return smartCropRendition;
    }

    public String getImageModifiers() {
        return imageModifiers;
    }
}