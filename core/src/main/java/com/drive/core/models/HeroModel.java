package com.drive.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private String alt;

    @ValueMapValue
    private String imageTitle;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private Calendar dateTime;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private String id;

    private String formattedDateTime;

    @PostConstruct
    protected void init() {
        formatDateTime();
    }

    private void formatDateTime() {

        if (dateTime == null) {
            return;
        }

        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy HH:mm");

        formattedDateTime =
                formatter.format(dateTime.getTime());
    }

    public String getTitle() {
        return title;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getAlt() {
        return alt;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public String[] getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }
}