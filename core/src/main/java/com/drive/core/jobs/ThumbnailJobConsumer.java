package com.drive.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=drive/assets/thumbnail"
        }
)
public class ThumbnailJobConsumer implements JobConsumer {

    private static final Logger LOG =
            LoggerFactory.getLogger(ThumbnailJobConsumer.class);

    @Override
    public JobResult process(Job job) {
        String assetPath =
                job.getProperty("assetPath", String.class);

        if (assetPath == null || assetPath.isBlank()) {
            LOG.warn("Missing asset path in thumbnail job");
            return JobResult.CANCEL;
        }

        LOG.info("Processing asset for thumbnail: {}", assetPath);

        // Actual thumbnail generation will be added later.

        LOG.info("Thumbnail job completed for: {}", assetPath);

        return JobResult.OK;
    }
}
