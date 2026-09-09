package com.drive.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/drive/hero/validate-id")
public class HeroIdValidationServlet
        extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();


        String id = request.getParameter("id");
        String componentPath =
                request.getParameter("componentPath");

        /*
         * Check ID
         */
        if (id == null || id.trim().isEmpty()) {

            result.put("valid", false);
            result.put(
                    "message",
                    "ID is required."
            );

            response.getWriter()
                    .write(result.toString());

            return;
        }

        /*
         * Check component path
         */
        if (componentPath == null ||
                componentPath.trim().isEmpty()) {

            result.put("valid", false);
            result.put(
                    "message",
                    "Unable to determine component path."
            );

            response.getWriter()
                    .write(result.toString());

            return;
        }

        id = id.trim();
        componentPath = componentPath.trim();

        Resource currentResource =
                request.getResourceResolver()
                        .getResource(componentPath);

        if (currentResource == null) {

            result.put("valid", false);
            result.put(
                    "message",
                    "Unable to find component."
            );

            response.getWriter()
                    .write(result.toString());

            return;
        }


        PageManager pageManager =
                request.getResourceResolver()
                        .adaptTo(PageManager.class);

        if (pageManager == null) {

            result.put("valid", false);
            result.put(
                    "message",
                    "Unable to find PageManager."
            );

            response.getWriter()
                    .write(result.toString());

            return;
        }


        Page currentPage =
                pageManager.getContainingPage(
                        currentResource
                );

        if (currentPage == null) {

            result.put("valid", false);
            result.put(
                    "message",
                    "Unable to find containing page."
            );

            response.getWriter()
                    .write(result.toString());

            return;
        }


        boolean duplicate =
                findDuplicateId(
                        currentPage.getContentResource(),
                        id,
                        componentPath
                );

        if (duplicate) {

            result.put("valid", false);
            result.put(
                    "message",
                    "This ID already exist on the page, please enter a unique ID."

            );

        } else {

            result.put("valid", true);
            result.put("message", "");
        }

        response.getWriter()
                .write(result.toString());
    }



    private boolean findDuplicateId(
            Resource resource,
            String id,
            String currentComponentPath) {

        if (resource == null) {
            return false;
        }


        if (!resource.getPath()
                .equals(currentComponentPath)) {

            String existingId =
                    resource.getValueMap()
                            .get("id", String.class);


            if (existingId != null &&
                    id.equals(existingId.trim())) {

                return true;
            }
        }

        /*
         * Search child resources recursively
         */
        for (Resource child :
                resource.getChildren()) {

            if (findDuplicateId(
                    child,
                    id,
                    currentComponentPath)) {

                return true;
            }
        }

        return false;
    }
}