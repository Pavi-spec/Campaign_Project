package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FlowerCampaignContentModel {

    @ValueMapValue
    private String eyebrow;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String secondaryDescription;

    @ValueMapValue
    private String highlightsHeading;

    @ValueMapValue
    private String highlight1;

    @ValueMapValue
    private String highlight2;

    @ValueMapValue
    private String highlight3;

    @ValueMapValue
    private String highlight4;

    @ValueMapValue
    private String section1Heading;

    @ValueMapValue
    private String section1Description;

    @ValueMapValue
    private String section2Heading;

    @ValueMapValue
    private String section2Description;

    @ValueMapValue
    private String section3Heading;

    @ValueMapValue
    private String section3Description;

    @ValueMapValue
    private String section4Heading;

    @ValueMapValue
    private String section4Description;

    @ValueMapValue
    private String linksHeading;

    @ValueMapValue
    private String link1Text;

    @ValueMapValue
    private String link1Path;

    @ValueMapValue
    private String link2Text;

    @ValueMapValue
    private String link2Path;

    @ValueMapValue
    private String link3Text;

    @ValueMapValue
    private String link3Path;

    @ValueMapValue
    private String link4Text;

    @ValueMapValue
    private String link4Path;

    @ValueMapValue
    private String link5Text;

    @ValueMapValue
    private String link5Path;

    @ValueMapValue
    private String offerHeading;

    @ValueMapValue
    private String offerDescription;

    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private String buttonPath;

    public String getEyebrow() {
        return eyebrow;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getSecondaryDescription() {
        return secondaryDescription;
    }

    public String getHighlightsHeading() {
        return highlightsHeading;
    }

    public String getHighlight1() {
        return highlight1;
    }

    public String getHighlight2() {
        return highlight2;
    }

    public String getHighlight3() {
        return highlight3;
    }

    public String getHighlight4() {
        return highlight4;
    }

    public String getSection1Heading() {
        return section1Heading;
    }

    public String getSection1Description() {
        return section1Description;
    }

    public String getSection2Heading() {
        return section2Heading;
    }

    public String getSection2Description() {
        return section2Description;
    }

    public String getSection3Heading() {
        return section3Heading;
    }

    public String getSection3Description() {
        return section3Description;
    }

    public String getSection4Heading() {
        return section4Heading;
    }

    public String getSection4Description() {
        return section4Description;
    }

    public String getLinksHeading() {
        return linksHeading;
    }

    public String getLink1Text() {
        return link1Text;
    }

    public String getLink1Path() {
        return link1Path;
    }

    public String getLink2Text() {
        return link2Text;
    }

    public String getLink2Path() {
        return link2Path;
    }

    public String getLink3Text() {
        return link3Text;
    }

    public String getLink3Path() {
        return link3Path;
    }

    public String getLink4Text() {
        return link4Text;
    }

    public String getLink4Path() {
        return link4Path;
    }

    public String getLink5Text() {
        return link5Text;
    }

    public String getLink5Path() {
        return link5Path;
    }

    public String getOfferHeading() {
        return offerHeading;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonPath() {
        return buttonPath;
    }
}