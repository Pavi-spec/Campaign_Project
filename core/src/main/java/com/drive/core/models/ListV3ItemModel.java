package com.drive.core.models;

import com.day.cq.wcm.api.Page;

import java.util.Calendar;

public class ListV3ItemModel {

    private final Page page;

    private final String displayTitle;

    private final String description;

    private final String path;

    private final String url;

    private final Calendar lastModified;

    public ListV3ItemModel(Page page) {

        this.page = page;
        this.displayTitle = getDisplayTitle(page);
        this.description = getDescription(page);
        this.path = getPath(page);
        this.url = getUrl(page);
        this.lastModified = getLastModified(page);
    }

    public Page getPage() {
        return page;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    private String getDisplayTitle(Page page) {

        if (page == null) {
            return "";
        }

        if (page.getTitle() != null
                && !page.getTitle().trim().isEmpty()) {

            return page.getTitle();
        }

        if (page.getNavigationTitle() != null
                && !page.getNavigationTitle().trim().isEmpty()) {

            return page.getNavigationTitle();
        }

        if (page.getName() != null
                && !page.getName().trim().isEmpty()) {

            return page.getName();
        }

        return "";
    }

    public String getDescription() {
        return description;
    }

    private String getDescription(Page page) {

        if (page == null) {
            return "";
        }

        if (page.getDescription() != null) {
            return page.getDescription();
        }

        return "";
    }

    public String getPath() {
        return path;
    }

    private String getPath(Page page) {

        if (page == null) {
            return "";
        }

        return page.getPath();
    }

    public String getUrl() {
        return url;
    }

    private String getUrl(Page page) {

        if (page == null) {
            return "";
        }

        return page.getPath() + ".html";
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    private Calendar getLastModified(Page page) {

        if (page != null
                && page.getLastModified() != null) {

            return page.getLastModified();
        }

        Calendar defaultDate =
                Calendar.getInstance();

        defaultDate.clear();

        defaultDate.set(
                1970,
                Calendar.JANUARY,
                1
        );

        return defaultDate;
    }

    public String getName() {

        if (page == null) {
            return "";
        }

        return page.getName();
    }

    public boolean isHideInNavigation() {

        if (page == null) {
            return false;
        }

        return page.getProperties()
                .get("hideInNav", false);
    }
}