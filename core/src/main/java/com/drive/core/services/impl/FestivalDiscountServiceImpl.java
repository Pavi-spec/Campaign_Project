package com.drive.core.services.impl;

import com.drive.core.services.DiscountService;
import org.osgi.service.component.annotations.Component;

@Component(service = DiscountService.class)
public class FestivalDiscountServiceImpl implements DiscountService {

    @Override
    public double calculateDiscount(double price) {
        return price * 0.20;
    }

    @Override
    public String getType() {
        return "festival";
    }
}