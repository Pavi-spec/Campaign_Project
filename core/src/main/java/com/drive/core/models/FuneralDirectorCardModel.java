package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FuneralDirectorCardModel {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String directionsLink;

    @ValueMapValue
    private String weekdayHours;

    @ValueMapValue
    private String weekendHours;

    @ValueMapValue
    private String phone;

    @ValueMapValue
    private String phoneButtonText;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDirectionsLink() {
        return directionsLink;
    }

    public String getWeekdayHours() {
        return weekdayHours;
    }

    public String getWeekendHours() {
        return weekendHours;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneButtonText() {
        return phoneButtonText != null && !phoneButtonText.isBlank()
                ? phoneButtonText : phone;
    }
}