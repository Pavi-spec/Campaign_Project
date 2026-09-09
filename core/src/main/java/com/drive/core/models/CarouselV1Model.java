package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CarouselV1Model {

    @ValueMapValue
    private String id;

    @ValueMapValue
    private String accessibilityLabel;

    @ValueMapValue
    private boolean autoplay;

    @ValueMapValue
    private long delay;

    @ValueMapValue
    private boolean autopauseDisabled;

    @ValueMapValue
    private String accessibilityPrevious;

    @ValueMapValue
    private String accessibilityNext;

    @ValueMapValue
    private String accessibilityPause;

    @ValueMapValue
    private String accessibilityPlay;

    @ValueMapValue
    private String accessibilityTablist;

    @ValueMapValue
    private boolean accessibilityAutoItemTitles;

    @ValueMapValue
    private String activeItem;

    @ValueMapValue
    private boolean controlsPrepended;

    public String getId() {
        return id;
    }

    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isAutopauseDisabled() {
        return autopauseDisabled;
    }

    public String getAccessibilityPrevious() {
        return accessibilityPrevious;
    }

    public String getAccessibilityNext() {
        return accessibilityNext;
    }

    public String getAccessibilityPause() {
        return accessibilityPause;
    }

    public String getAccessibilityPlay() {
        return accessibilityPlay;
    }

    public String getAccessibilityTablist() {
        return accessibilityTablist;
    }

    public boolean isAccessibilityAutoItemTitles() {
        return accessibilityAutoItemTitles;
    }

    public String getActiveItem() {
        return activeItem;
    }

    public boolean isControlsPrepended() {
        return controlsPrepended;
    }
}

