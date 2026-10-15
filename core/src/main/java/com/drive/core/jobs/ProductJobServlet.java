
package com.drive.core.jobs;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/bin/drive/submit-jobs"
        }
)
public class JobTriggerServlet extends SlingSafeMethodsServlet {

    @Reference
    private MyJobService myJobService;

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        int submitted = myJobService.submitJobs();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "Successfully submitted " + submitted
                        + " out of 3 Sling Jobs. Check the AEM log."
        );
    }
}
