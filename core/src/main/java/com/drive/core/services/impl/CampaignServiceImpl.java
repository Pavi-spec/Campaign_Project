package com.drive.core.services.impl;

import com.drive.core.services.CampaignService;
import org.osgi.service.component.annotations.Component;

@Component(service = CampaignService.class)
public class CampaignServiceImpl implements CampaignService {

    @Override
    public String getCampaignMessage() {
        return "Drive Campaign Portal Running";
    }
}