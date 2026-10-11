package com.drive.core.services.impl;

import com.drive.core.services.MessageService;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.annotations.Component;

@Component(service = MessageService.class)
@ServiceRanking(1002)
public class FunnyMessageServiceImpl implements MessageService {

    @Override
    public String getMessage() {

        return "Bring popcorn also 😄";

    }
}
