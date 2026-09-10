package com.drive.core.models;

import com.drive.core.services.CampaignDataService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CampaignModel {

    @ValueMapValue
    private String title;

    @OSGiService
    private CampaignDataService campaignService;

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return campaignService.getMessage(title);
    }
}

