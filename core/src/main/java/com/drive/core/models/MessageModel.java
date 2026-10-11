package com.drive.core.models;

import com.drive.core.services.MessageService;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

@Model(adaptables = Resource.class)
public class MessageModel {

    @OSGiService(
            filter = "(component.name=com.drive.core.services.impl.FunnyMessageServiceImpl)"
    )
    private MessageService funnyService;

    @OSGiService(
            filter = "(component.name=com.drive.core.services.impl.MessageServiceImpl)"
    )
    private MessageService normalService;

    @OSGiService(
            filter = "(component.name=com.drive.core.services.impl.TamilMessageServiceImpl)"
    )
    private MessageService tamilService;

    public String getFunnyMessage() {
        return funnyService.getMessage();
    }

    public String getNormalMessage() {
        return normalService.getMessage();
    }

    public String getTamilMessage() {
        return tamilService.getMessage();
    }
}