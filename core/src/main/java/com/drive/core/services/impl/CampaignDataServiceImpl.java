package com.drive.core.services.impl;

import com.drive.core.services.CampaignDataService;
import org.osgi.service.component.annotations.Component;

@Component(service = CampaignDataService.class)
public class CampaignDataServiceImpl implements CampaignDataService {

    @Override
    public String getMessage(String title) {

        return "Welcome to " + title;
    }
}
