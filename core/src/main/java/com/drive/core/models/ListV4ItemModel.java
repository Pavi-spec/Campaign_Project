package com.drive.core.models;

import com.day.cq.wcm.api.Page;

import java.util.Calendar;

public class ListV4ItemModel {

    private final Page page;
    private final String displayTitle;
    private final String description;
    private final String path;
    private final String url;
    private final Calendar lastModified;
    private final String linkTarget;
    private final boolean linked;
    private final boolean staticItem;

    public ListV4ItemModel(Page page, boolean linkItems) {

        this.page = page;
        this.staticItem = false;

        if (page != null) {

            this.displayTitle = getPageTitle(page);
            this.description = page.getDescription();
            this.path = page.getPath();
            this.url = page.getPath() + ".html";
            this.lastModified = getPageLastModified(page);
            this.linkTarget = "_self";
            this.linked = linkItems;

        } else {

            this.displayTitle = "";
            this.description = "";
            this.path = "";
            this.url = "";
            this.lastModified = null;
            this.linkTarget = "_self";
            this.linked = false;
        }
    }

    public ListV4ItemModel(
            String linkURL,
            String linkText,
            String linkTarget
    ) {

        this.page = null;
        this.staticItem = true;

        this.displayTitle =
                linkText != null ? linkText : "";

        this.description = "";

        String normalizedUrl = normalizeUrl(linkURL);

        this.path = normalizedUrl;
        this.url = normalizedUrl;
        this.lastModified = null;
        this.linkTarget = normalizeLinkTarget(linkTarget);
        this.linked = !this.url.isEmpty();
    }

    private String normalizeUrl(String linkURL) {

        if (linkURL == null || linkURL.trim().isEmpty()) {
            return "";
        }

        linkURL = linkURL.trim();

        if (linkURL.startsWith("/content/")
                && !linkURL.endsWith(".html")) {

            return linkURL + ".html";
        }

        return linkURL;
    }

    private String getPageTitle(Page page) {

        if (page == null) {
            return "";
        }

        if (page.getTitle() != null
                && !page.getTitle().trim().isEmpty()) {

            return page.getTitle();
        }

        if (page.getPageTitle() != null
                && !page.getPageTitle().trim().isEmpty()) {

            return page.getPageTitle();
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
    private Calendar getPageLastModified(Page page) {

        Calendar lastModified =
                page.getLastModified();

        if (lastModified == null
                && page.getContentResource() != null) {

            lastModified = page.getContentResource()
                    .getValueMap()
                    .get(
                            "cq:lastModified",
                            Calendar.class
                    );
        }

        return lastModified;
    }

    private String normalizeLinkTarget(String target) {

        if ("_blank".equals(target)) {
            return "_blank";
        }

        return "_self";
    }

    public Page getPage() {
        return page;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    public boolean isLinked() {
        return linked;
    }

    public boolean isStaticItem() {
        return staticItem;
    }
}
