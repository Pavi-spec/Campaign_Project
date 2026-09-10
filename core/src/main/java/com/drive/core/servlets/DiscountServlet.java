package com.drive.core.servlets;

import com.drive.core.services.DiscountService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/discount",
                "sling.servlet.methods=GET"
        }
)
public class DiscountServlet extends SlingSafeMethodsServlet {

    @Reference(
            service = DiscountService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    private volatile List<DiscountService> discountServices;

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        String type = request.getParameter("type");
        String priceParameter = request.getParameter("price");

        if (type == null || priceParameter == null) {

            response.setStatus(
                    SlingHttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write(
                    "type and price are required"
            );

            return;
        }

        double price = Double.parseDouble(priceParameter);

        DiscountService selectedService = null;

        for (DiscountService service : discountServices) {

            if (service.getType().equalsIgnoreCase(type)) {
                selectedService = service;
                break;
            }
        }

        if (selectedService == null) {

            response.setStatus(
                    SlingHttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write(
                    "Invalid discount type"
            );

            return;
        }

        double discount =
                selectedService.calculateDiscount(price);

        double finalPrice =
                price - discount;

        response.setContentType("application/json");

        response.getWriter().write(
                "{"
                        + "\"type\":\"" + type + "\","
                        + "\"price\":" + price + ","
                        + "\"discount\":" + discount + ","
                        + "\"finalPrice\":" + finalPrice
                        + "}"
        );
    }
}