
package com.drive.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.drive.core.services.LoginDataService;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/drive/login-data")
public class LoginDataServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private LoginDataService loginDataService;

    @Override
    protected void doPost(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");

        boolean passwordRequired =
                "true".equals(request.getParameter("passwordRequired"));

        if ((email == null || email.trim().isEmpty())
                && !passwordRequired) {

            response.setStatus(
                    SlingHttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().write(
                    "{\"success\":false,"
                            + "\"message\":\"Email or password field configuration is required.\"}");
            return;
        }

        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(
                ResourceResolverFactory.SUBSERVICE,
                "loginDataWriter");

        try (ResourceResolver resolver =
                     resourceResolverFactory.getServiceResourceResolver(
                             authInfo)) {

            boolean saved = loginDataService.saveLoginData(
                    resolver,
                    email,
                    passwordRequired);

            if (saved) {
                response.getWriter().write(
                        "{\"success\":true,"
                                + "\"message\":\"Login submission saved.\"}");
            } else {
                response.setStatus(
                        SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                response.getWriter().write(
                        "{\"success\":false,"
                                + "\"message\":\"Unable to save login submission.\"}");
            }

        } catch (LoginException e) {
            response.setStatus(
                    SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            response.getWriter().write(
                    "{\"success\":false,"
                            + "\"message\":\"Unable to access login storage.\"}");
        }
    }
}
