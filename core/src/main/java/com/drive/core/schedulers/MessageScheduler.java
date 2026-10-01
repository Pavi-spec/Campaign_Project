package com.drive.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = Runnable.class,
        immediate = true
)
public class MessageScheduler implements Runnable {

    private static final Logger LOG =
            LoggerFactory.getLogger(MessageScheduler.class);

    private static final String SCHEDULER_NAME =
            "drive-message-scheduler";

    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate() {

        LOG.info("Message Scheduler ACTIVATED");

        ScheduleOptions options =
                scheduler.EXPR("*/5 * * * * ?");

        options.name(SCHEDULER_NAME);
        options.canRunConcurrently(false);

        scheduler.schedule(this, options);

        LOG.info("Message Scheduler scheduled successfully.");
    }

    @Deactivate
    protected void deactivate() {

        scheduler.unschedule(SCHEDULER_NAME);

        LOG.info("Message Scheduler DEACTIVATED");
    }

    @Override
    public void run() {

        LOG.info("Hello! Scheduler is running.");
    }
}