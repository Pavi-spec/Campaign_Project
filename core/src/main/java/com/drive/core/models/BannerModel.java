package com.drive.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BannerModel {


    @ValueMapValue
    private String bannerType;


    @ValueMapValue
    private String logo;

    @ValueMapValue
    private String logoAlt;

    @ChildResource(name = "navigationItems")
    private List<NavigationItem> navigationItems;

    @ValueMapValue
    private String searchPlaceholder;

    @ValueMapValue
    private String searchButtonText;



    @ValueMapValue
    private String primaryTitle;

    @ValueMapValue
    private String primaryDescription;

    @ValueMapValue
    private String backgroundColor;



    @ValueMapValue
    private String secondaryTitle;

    @ValueMapValue
    private String secondaryDescription;

    @ValueMapValue
    private String secondaryImage;

    @ValueMapValue
    private String secondaryImageAlt;


    @ValueMapValue
    private String contactHeading;

    @ValueMapValue
    private String contactNameLabel;

    @ValueMapValue
    private String contactName;

    @ValueMapValue
    private String contactPhoneLabel;

    @ValueMapValue
    private String contactPhone;

    @ValueMapValue
    private String contactEmailLabel;

    @ValueMapValue
    private String contactEmail;

    @ValueMapValue
    private String contactAddressLabel;

    @ValueMapValue
    private String contactAddress;



    @ValueMapValue
    private String footerText;



    public String getBannerType() {
        return bannerType;
    }

    public String getLogo() {
        return logo;
    }

    public String getLogoAlt() {
        return logoAlt;
    }

    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public String getSearchPlaceholder() {
        return searchPlaceholder;
    }

    public String getSearchButtonText() {
        return searchButtonText;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public String getPrimaryDescription() {
        return primaryDescription;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public String getSecondaryDescription() {
        return secondaryDescription;
    }

    public String getSecondaryImage() {
        return secondaryImage;
    }

    public String getSecondaryImageAlt() {
        return secondaryImageAlt;
    }

    public String getContactHeading() {
        return contactHeading;
    }

    public String getContactNameLabel() {
        return contactNameLabel;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhoneLabel() {
        return contactPhoneLabel;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactEmailLabel() {
        return contactEmailLabel;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactAddressLabel() {
        return contactAddressLabel;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public String getFooterText() {
        return footerText;
    }
}