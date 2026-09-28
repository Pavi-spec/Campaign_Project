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
        resourceTypes = "drive/components/combine",
        selectors = "html",
        methods = HttpConstants.METHOD_GET
)
public class HtmlResponseServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");

        response.getWriter().write(
                "<h1>Welcome to AEM</h1>" +
                        "<p>This is an HTML response from Servlet.</p>"
        );
    }
}