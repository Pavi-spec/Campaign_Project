package com.drive.core.thumbnail;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.sling.event.jobs.JobManager;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/dam",
                ResourceChangeListener.CHANGES + "=ADDED"
        }
)
public class ThumbnailUploadListener
        implements ResourceChangeListener {

    private static final Logger LOG =
            LoggerFactory.getLogger(ThumbnailUploadListener.class);

    @Reference
    private JobManager jobManager;

    @Override
    public void onChange(List<ResourceChange> changes) {

        for (ResourceChange change : changes) {

            String path = change.getPath();

            if (path == null) {
                continue;
            }


            if (path.contains("/jcr:content")) {
                continue;
            }


            if (path.matches(
                    "(?i)^/content/dam/.+\\.(jpg|jpeg|png|webp)$")) {

                Map<String, Object> properties = new HashMap<>();
                properties.put("assetPath", path);

                LOG.info("Thumbnail upload detected: {}", path);

                if (jobManager.addJob(
                        "drive/thumbnail/generate",
                        properties) != null) {

                    LOG.info("Thumbnail job submitted: {}", path);

                } else {
                    LOG.error("Could not submit thumbnail job: {}", path);
                }
            }
        }
    }
}
