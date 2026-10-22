package com.drive.core.listeners;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drive.core.services.AssetJobService;

@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/dam",
                ResourceChangeListener.CHANGES + "=ADDED"
        }
)
public class AssetUploadListener implements ResourceChangeListener {

    private static final Logger LOG =
            LoggerFactory.getLogger(AssetUploadListener.class);

    @Reference
    private AssetJobService assetJobService;

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

                LOG.info("New image detected: {}", path);

                boolean submitted =
                        assetJobService.submitThumbnailJob(path);

                LOG.info("Thumbnail job submitted: {}", submitted);
            }
        }
    }

}

