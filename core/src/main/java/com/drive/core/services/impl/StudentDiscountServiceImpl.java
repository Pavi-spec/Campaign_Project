package com.drive.core.services.impl;

import com.drive.core.services.DiscountService;
import org.osgi.service.component.annotations.Component;

@Component(service = DiscountService.class)
public class StudentDiscountServiceImpl implements DiscountService {

    @Override
    public double calculateDiscount(double price) {
        return price * 0.10;
    }

    @Override
    public String getType() {
        return "student";
    }
}