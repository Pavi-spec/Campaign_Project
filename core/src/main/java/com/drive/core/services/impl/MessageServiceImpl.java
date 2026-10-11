package com.drive.core.services.impl;
import org.osgi.service.component.propertytypes.ServiceRanking;
import com.drive.core.services.MessageService;
import org.osgi.service.component.annotations.Component;


@Component(service = MessageService.class)
@ServiceRanking(3000)
public class MessageServiceImpl implements MessageService {

    @Override
    public String getMessage() {

        return "We are planning to go Movie coming Saturday";

    }
}

