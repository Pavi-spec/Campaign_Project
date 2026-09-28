package com.drive.core.servlets;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/drive/login")
public class LoginServlet extends SlingAllMethodsServlet {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$"
            );

    @Override
    protected void doPost(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        String emailMessage = "";
        String passwordMessage = "";



        if (email == null || email.trim().isEmpty()) {

            emailMessage = "Email is required.";

        } else {

            email = email.trim();

            if (!EMAIL_PATTERN.matcher(email).matches()) {

                emailMessage =
                        "Please enter a valid email address.";
            }
        }




        if (password == null || password.isEmpty()) {

            passwordMessage =
                    "Password is required.";

        } else if (password.length() < 8) {

            passwordMessage =
                    "Password must contain at least 8 characters.";

        } else if (!password.matches(".*[A-Z].*")) {

            passwordMessage =
                    "Password must contain at least one uppercase letter.";

        } else if (!password.matches(".*[a-z].*")) {

            passwordMessage =
                    "Password must contain at least one lowercase letter.";

        } else if (!password.matches(".*[0-9].*")) {

            passwordMessage =
                    "Password must contain at least one number.";

        } else if (!password.matches(".*[^A-Za-z0-9].*")) {

            passwordMessage =
                    "Password must contain at least one special character.";

        } else if (email != null
                && !email.isEmpty()
                && password.equalsIgnoreCase(email)) {

            passwordMessage =
                    "Password must be different from your email.";
        }



        boolean success =
                emailMessage.isEmpty()
                        && passwordMessage.isEmpty();


        sendResponse(
                response,
                success,
                emailMessage,
                passwordMessage
        );
    }


    private void sendResponse(
            SlingHttpServletResponse response,
            boolean success,
            String emailMessage,
            String passwordMessage)
            throws IOException {

        String json =
                "{"
                        + "\"success\":" + success + ","
                        + "\"emailMessage\":\""
                        + escapeJson(emailMessage)
                        + "\","
                        + "\"passwordMessage\":\""
                        + escapeJson(passwordMessage)
                        + "\""
                        + "}";

        response.getWriter().write(json);
    }


    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}

