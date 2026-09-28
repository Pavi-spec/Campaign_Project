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
        selectors = "twoquery",
        methods = HttpConstants.METHOD_GET
)
public class TwoQueryServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String name = request.getParameter("name");
        String age = request.getParameter("age");

        response.getWriter().write(
                "Name: " + name + "\n" +
                        "Age: " + age
        );
    }
}