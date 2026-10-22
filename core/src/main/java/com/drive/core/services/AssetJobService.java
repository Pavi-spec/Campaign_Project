package com.drive.core.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AssetJobService.class)
public class AssetJobService {

    @Reference
    private JobManager jobManager;

    public boolean submitThumbnailJob(String assetPath) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("assetPath", assetPath);

        return jobManager.addJob(
                "drive/assets/thumbnail",
                properties
        ) != null;
    }
}
