package com.drive.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqModel {

    @ChildResource(name = "courseDetails")
    private Resource courseDetails;

    public List<FaqItem> getItems() {

        List<FaqItem> items = new ArrayList<>();

        if (courseDetails != null) {

            for (Resource child : courseDetails.getChildren()) {

                items.add(new FaqItem(child));
            }
        }

        return items;
    }

    public static class FaqItem {

        private final Resource resource;

        public FaqItem(Resource resource) {
            this.resource = resource;
        }

        public String getTitle() {
            return resource.getValueMap()
                    .get("title", String.class);
        }

        public String getContent() {
            return resource.getValueMap()
                    .get("content", String.class);
        }
    }
}

