package com.drive.core.servlets;

import com.drive.core.services.MessageService;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/drive/message")
public class MessageServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private MessageService messageService;

    @Override
    protected void doGet(
            final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String message = messageService.getMessage();

        resp.getWriter().write("Hello Pavi, " + message);
    }
}
