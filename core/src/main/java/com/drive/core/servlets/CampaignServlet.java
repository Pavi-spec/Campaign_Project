package com.drive.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.osgi.service.component.annotations.Component;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/campaigns",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET
        }
)
public class CampaignServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        String apiUrl = "https://dummyjson.com/products?limit=8";

        URL url = new URL(apiUrl);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));

        StringBuilder result = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            result.append(line);

        }

        reader.close();

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}