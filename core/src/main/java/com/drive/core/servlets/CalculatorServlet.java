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
        selectors = "calculate",
        methods = HttpConstants.METHOD_GET
)
public class CalculatorServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String num1 = request.getParameter("num1");
        String num2 = request.getParameter("num2");
        String operation = request.getParameter("operation");

        double number1 = Double.parseDouble(num1);
        double number2 = Double.parseDouble(num2);

        double result;

        switch (operation) {

            case "add":
                result = number1 + number2;
                break;

            case "sub":
                result = number1 - number2;
                break;

            case "mul":
                result = number1 * number2;
                break;

            case "div":
                result = number1 / number2;
                break;

            default:
                response.getWriter().write(
                        "Invalid operation"
                );
                return;
        }

        response.getWriter().write(
                "Result: " + result
        );
    }
}