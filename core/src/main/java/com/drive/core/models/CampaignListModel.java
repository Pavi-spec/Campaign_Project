package com.drive.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CampaignListModel {

    @ChildResource(name = "campaigns")
    private List<CampaignItem> campaigns;

    public List<CampaignItem> getCampaigns() {
        return campaigns;
    }
}