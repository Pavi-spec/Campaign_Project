package com.drive.core.schedulers;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component(
        service = Runnable.class,
        immediate = true
)
public class PageDeletionScheduler implements Runnable {

    private static final Logger LOG =
            LoggerFactory.getLogger(PageDeletionScheduler.class);

    private static final String SCHEDULER_NAME =
            "drive-page-deletion-scheduler";

    private static final String PAGE_PATH =
            "/content/drive/us/en/demo";

    private static final String SUBSERVICE_NAME =
            "pageDeletionService";

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    protected void activate() {

        LOG.info("==========================================");
        LOG.info("Page Deletion Scheduler ACTIVATED");
        LOG.info("Target page: {}", PAGE_PATH);
        LOG.info("==========================================");

        long delay = 10 * 1000;

        Date executionTime =
                new Date(System.currentTimeMillis() + delay);

        ScheduleOptions options =
                scheduler.AT(executionTime);

        options.name(SCHEDULER_NAME);
        options.canRunConcurrently(false);

        boolean result =
                scheduler.schedule(this, options);

        LOG.info(
                "scheduler.schedule() returned: {}",
                result
        );

        if (result) {

            LOG.info(
                    "Page Deletion Scheduler scheduled successfully."
            );

            LOG.info(
                    "Page {} will be deleted after 10 seconds.",
                    PAGE_PATH
            );

        } else {

            LOG.error(
                    "Page Deletion Scheduler could NOT be scheduled."
            );
        }
    }

    @Deactivate
    protected void deactivate() {

        scheduler.unschedule(SCHEDULER_NAME);

        LOG.info(
                "Page Deletion Scheduler DEACTIVATED"
        );
    }

    @Override
    public void run() {

        LOG.info("==========================================");
        LOG.info("Scheduler run() STARTED");
        LOG.info("Target page: {}", PAGE_PATH);
        LOG.info("==========================================");

        Map<String, Object> serviceUser =
                Collections.singletonMap(
                        ResourceResolverFactory.SUBSERVICE,
                        SUBSERVICE_NAME
                );

        try (ResourceResolver resourceResolver =
                     resourceResolverFactory
                             .getServiceResourceResolver(serviceUser)) {

            LOG.info(
                    "ResourceResolver obtained successfully."
            );

            Resource resource =
                    resourceResolver.getResource(PAGE_PATH);

            if (resource == null) {

                LOG.warn(
                        "Page does not exist: {}",
                        PAGE_PATH
                );

                return;
            }

            LOG.info(
                    "Page resource found: {}",
                    resource.getPath()
            );

            PageManager pageManager =
                    resourceResolver.adaptTo(PageManager.class);

            if (pageManager == null) {

                LOG.error(
                        "Could not obtain PageManager."
                );

                return;
            }

            LOG.info(
                    "PageManager obtained successfully."
            );

            Page page =
                    pageManager.getPage(PAGE_PATH);

            if (page == null) {

                LOG.warn(
                        "Page not found: {}",
                        PAGE_PATH
                );

                return;
            }

            LOG.info(
                    "Page found: {}",
                    page.getPath()
            );

            LOG.info(
                    "Starting page deletion..."
            );

            pageManager.delete(page, true);

            LOG.info(
                    "pageManager.delete() completed."
            );

            resourceResolver.commit();

            LOG.info(
                    "resourceResolver.commit() completed."
            );

            LOG.info(
                    "Page deleted successfully: {}",
                    PAGE_PATH
            );

        } catch (Exception e) {

            LOG.error(
                    "ERROR while deleting page: {}",
                    PAGE_PATH,
                    e
            );
        }

        LOG.info("==========================================");
        LOG.info("Scheduler run() COMPLETED");
        LOG.info("==========================================");
    }
}