
package com.drive.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=drive/image/process"
        }
)
public class ImageJobConsumer implements JobConsumer {

    private static final Logger LOG =
            LoggerFactory.getLogger(ImageJobConsumer.class);

    @Override
    public JobResult process(Job job) {

        String imagePath =
                job.getProperty("imagePath", String.class);

        if (imagePath == null || imagePath.isEmpty()) {
            LOG.error("Job has no imagePath property");
            return JobResult.CANCEL;
        }

        LOG.info("Processing image: {}", imagePath);

        // Demonstration only: no actual image is modified.

        LOG.info("Finished processing image: {}", imagePath);

        return JobResult.OK;
    }
}
