package com.drive.core.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ProductJobService.class)
public class ProductJobService {

    @Reference
    private JobManager jobManager;

    public boolean submitProductJob(String productPath) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("productPath", productPath);

        return jobManager.addJob(
                "drive/products/sync",
                properties
        ) != null;
    }
}
