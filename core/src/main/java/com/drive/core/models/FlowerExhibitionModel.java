
package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FlowerExhibitionModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String viewAllText;

    @ValueMapValue
    private String viewAllLink;

    public String getHeading() {
        return heading != null && !heading.isBlank()
                ? heading
                : "Discover Our Flowers";
    }

    public String getViewAllText() {
        return viewAllText != null && !viewAllText.isBlank()
                ? viewAllText
                : "View all flowers";
    }

    public String getViewAllLink() {
        return viewAllLink;
    }

    public boolean isViewAllLinkAvailable() {
        return viewAllLink != null && !viewAllLink.isBlank();
    }
}
