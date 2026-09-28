package com.drive.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.servlets.HttpConstants;

import org.apache.sling.servlets.annotations.SlingServletResourceTypes;

import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "drive/components/campaign-button",
        selectors = "properties",
        methods = HttpConstants.METHOD_GET
)
public class ComponentPropertyServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String label = request.getResource()
                .getValueMap()
                .get("label", String.class);

        String link = request.getResource()
                .getValueMap()
                .get("link", String.class);

        String openInNewTab = request.getResource()
                .getValueMap()
                .get("openInNewTab", String.class);

        response.getWriter().write(
                "Label: " + label + "\n" +
                        "Link: " + link + "\n" +
                        "Open In New Tab: " + openInNewTab
        );
    }
}