package com.drive.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS
                        + "=drive/products/sync"
        }
)
public class ProductJobConsumer implements JobConsumer {

    private static final Logger LOG =
            LoggerFactory.getLogger(ProductJobConsumer.class);

    @Override
    public JobResult process(Job job) {

        String productPath =
                job.getProperty("productPath", String.class);

        if (productPath == null || productPath.isBlank()) {
            LOG.warn("Product job has no productPath");
            return JobResult.CANCEL;
        }

        LOG.info("Processing product job for: {}", productPath);


        LOG.info("Product job completed for: {}", productPath);

        return JobResult.OK;
    }
}
