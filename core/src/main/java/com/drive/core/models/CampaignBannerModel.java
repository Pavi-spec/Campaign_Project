package com.drive.core.models;

import com.drive.core.services.CampaignService;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CampaignBannerModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @OSGiService
    private CampaignService campaignService;

    private String serviceMessage;

    @PostConstruct
    protected void init() {
        serviceMessage = campaignService.getCampaignMessage();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getServiceMessage() {
        return serviceMessage;
    }
}