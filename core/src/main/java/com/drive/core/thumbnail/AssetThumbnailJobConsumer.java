package com.drive.core.thumbnail;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=drive/thumbnail/generate"
        }
)
public class AssetThumbnailJobConsumer implements JobConsumer {

    private static final Logger LOG =
            LoggerFactory.getLogger(AssetThumbnailJobConsumer.class);

    @Reference
    private ThumbnailGenerationService thumbnailGenerationService;

    @Override
    public JobResult process(Job job) {

        String assetPath =
                job.getProperty("assetPath", String.class);

        if (assetPath == null || assetPath.isBlank()) {
            LOG.error("Thumbnail job has no asset path");
            return JobResult.CANCEL;
        }

        LOG.info("Starting thumbnail generation for: {}", assetPath);

        boolean success =
                thumbnailGenerationService.generateThumbnail(assetPath);

        if (success) {
            LOG.info("Thumbnail generated successfully for: {}", assetPath);
            return JobResult.OK;
        }

        LOG.error("Thumbnail generation failed for: {}", assetPath);


        return JobResult.FAILED;
    }
}
