package com.drive.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FuneralIntroModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String secondDescription;

    @ValueMapValue
    private String highlight1;

    @ValueMapValue
    private String highlight2;

    @ValueMapValue
    private String highlight3;

    @ValueMapValue
    private String highlight4;

    @ValueMapValue
    private String teamName;

    @ValueMapValue
    private String teamRole;

    @ValueMapValue
    private String teamImage;

    @ValueMapValue
    private String teamAlt;

    @ValueMapValue
    private String teamDescription;

    @ValueMapValue
    private String teamQuote;

    @ValueMapValue
    private String teamLinkText;

    @ValueMapValue
    private String teamLink;

    @ValueMapValue
    private String facilitiesHeading;

    @ValueMapValue
    private String parkingHeading;

    @ValueMapValue
    private String parkingDescription;

    @ValueMapValue
    private String roomHeading;

    @ValueMapValue
    private String roomDescription;

    @ValueMapValue
    private String venuesHeading;

    @ValueMapValue
    private String venuesDescription;

    @ValueMapValue
    private String church1;

    @ValueMapValue
    private String church1Link;

    @ValueMapValue
    private String church2;

    @ValueMapValue
    private String church2Link;

    @ValueMapValue
    private String church3;

    @ValueMapValue
    private String church3Link;

    @ValueMapValue
    private String church4;

    @ValueMapValue
    private String church4Link;

    @ValueMapValue
    private String church5;

    @ValueMapValue
    private String church5Link;

    @ValueMapValue
    private String church6;

    @ValueMapValue
    private String church6Link;

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getSecondDescription() {
        return secondDescription;
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

    public String getTeamName() {
        return teamName;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public String getTeamImage() {
        return teamImage;
    }

    public String getTeamAlt() {
        return teamAlt;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public String getTeamQuote() {
        return teamQuote;
    }

    public String getTeamLinkText() {
        return teamLinkText;
    }

    public String getTeamLink() {
        return teamLink;
    }

    public String getFacilitiesHeading() {
        return facilitiesHeading;
    }

    public String getParkingHeading() {
        return parkingHeading;
    }

    public String getParkingDescription() {
        return parkingDescription;
    }

    public String getRoomHeading() {
        return roomHeading;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public String getVenuesHeading() {
        return venuesHeading;
    }

    public String getVenuesDescription() {
        return venuesDescription;
    }

    public String getChurch1() {
        return church1;
    }

    public String getChurch1Link() {
        return church1Link;
    }

    public String getChurch2() {
        return church2;
    }

    public String getChurch2Link() {
        return church2Link;
    }

    public String getChurch3() {
        return church3;
    }

    public String getChurch3Link() {
        return church3Link;
    }

    public String getChurch4() {
        return church4;
    }

    public String getChurch4Link() {
        return church4Link;
    }

    public String getChurch5() {
        return church5;
    }

    public String getChurch5Link() {
        return church5Link;
    }

    public String getChurch6() {
        return church6;
    }

    public String getChurch6Link() {
        return church6Link;
    }
}
