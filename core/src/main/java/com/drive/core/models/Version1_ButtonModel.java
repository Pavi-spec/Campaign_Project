package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Version1_ButtonModel {

    @ValueMapValue(name = "jcr:title")
    private String text;

    @ValueMapValue
    private String link;

    @ValueMapValue
    private String icon;

    @ValueMapValue
    private String id;

    @ValueMapValue
    private String accessibilityLabel;

    public String getText() {
        return text;
    }

    public String getLink() {

        if (link == null || link.isEmpty()) {
            return null;
        }

        String url = link;

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        if (url.startsWith("/content/") && !url.endsWith(".html")) {
            url = url + ".html";
        }

        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }
}