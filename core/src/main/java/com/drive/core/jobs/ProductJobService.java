
package com.drive.core.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = MyJobService.class)
public class MyJobService {

    private static final Logger LOG =
            LoggerFactory.getLogger(MyJobService.class);

    private static final String JOB_TOPIC =
            "drive/image/process";

    @Reference
    private JobManager jobManager;

    public int submitJobs() {

        String[] images = {
                "/content/dam/image1.jpg",
                "/content/dam/image2.jpg",
                "/content/dam/image3.jpg"
        };

        int submitted = 0;

        for (String imagePath : images) {

            Map<String, Object> properties = new HashMap<>();
            properties.put("imagePath", imagePath);

            Job job = jobManager.addJob(JOB_TOPIC, properties);

            if (job != null) {
                submitted++;
                LOG.info("Submitted job for image: {}", imagePath);
            } else {
                LOG.error("Failed to submit job for image: {}", imagePath);
            }
        }

        return submitted;
    }
}
