package com.drive.core.servlets;

import com.drive.core.services.SearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/drive/search"})
public class SearchServlet extends SlingAllMethodsServlet {

    private static final Logger LOG =
            LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    private SearchService searchService;

    @Override
    protected void doGet(
            SlingHttpServletRequest req,
            SlingHttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String searchText = req.getParameter("searchText");
            String pageValue = req.getParameter("pageNumber");
            String sizeValue = req.getParameter("resultPerPage");

            if (searchText == null || searchText.trim().isEmpty()
                    || pageValue == null || sizeValue == null) {

                resp.setStatus(
                        SlingHttpServletResponse.SC_BAD_REQUEST);

                resp.getWriter().write(
                        new JSONObject()
                                .put("error",
                                        "Provide searchText, pageNumber and resultPerPage")
                                .toString());
                return;
            }

            int pageNumber = Integer.parseInt(pageValue);
            int resultPerPage = Integer.parseInt(sizeValue);

            if (pageNumber < 1 || resultPerPage < 1) {
                resp.setStatus(
                        SlingHttpServletResponse.SC_BAD_REQUEST);

                resp.getWriter().write(
                        new JSONObject()
                                .put("error",
                                        "Page number and resultPerPage must be positive")
                                .toString());
                return;
            }

            int startResult =
                    (pageNumber - 1) * resultPerPage;

            JSONObject result = searchService.searchResult(
                    searchText,
                    startResult,
                    resultPerPage);

            resp.getWriter().write(result.toString());

        } catch (NumberFormatException e) {
            resp.setStatus(
                    SlingHttpServletResponse.SC_BAD_REQUEST);

            resp.getWriter().write(
                    new JSONObject()
                            .put("error", "Invalid pagination values")
                            .toString());

        } catch (Exception e) {
            LOG.error("Error in SearchServlet", e);

            resp.setStatus(
                    SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            resp.getWriter().write(
                    new JSONObject()
                            .put("error", "Unable to search pages")
                            .toString());
        }
    }
}
