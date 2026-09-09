package com.drive.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CompositeModel {

    @Self
    private Resource resource;

    private List<Resource> selectorItems = new ArrayList<>();

    @PostConstruct
    protected void init() {

        Resource itemsResource = resource.getChild("selectorItems");

        if (itemsResource != null) {
            for (Resource item : itemsResource.getChildren()) {
                selectorItems.add(item);
            }
        }
    }
    public List<Resource> getSelectorItems() {
        return selectorItems;
    }
}