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
                "sling.servlet.paths=/bin/product-job",
                "sling.servlet.methods=GET"
        }
)
public class ProductJobServlet extends SlingSafeMethodsServlet {

    @Reference
    private ProductJobService productJobService;

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        String productPath = request.getParameter("path");

        if (productPath == null || productPath.isBlank()
                || !productPath.startsWith("/content/")) {
            response.sendError(
                    SlingHttpServletResponse.SC_BAD_REQUEST,
                    "Provide a valid product path under /content/"
            );
            return;
        }

        boolean submitted =
                productJobService.submitProductJob(productPath);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        if (submitted) {
            response.getWriter().println(
                    "Product job submitted: " + productPath
            );
        } else {
            response.setStatus(
                    SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.getWriter().println(
                    "Could not submit product job"
            );
        }
    }
}
