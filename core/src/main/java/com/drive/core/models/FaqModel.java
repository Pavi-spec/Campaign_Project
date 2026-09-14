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

    @ChildResource(name = "accordionItems")
    private Resource accordionItems;

    public List<FaqItem> getItems() {

        List<FaqItem> items = new ArrayList<>();

        if (accordionItems != null) {

            for (Resource child : accordionItems.getChildren()) {

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

        public String getDescription() {
            return resource.getValueMap()
                    .get("description", String.class);
        }

        public String getTextFieldLabel() {
            return resource.getValueMap()
                    .get("textFieldLabel", String.class);
        }

        public String getTextFieldPlaceholder() {
            return resource.getValueMap()
                    .get("textFieldPlaceholder", String.class);
        }

        public String getTextareaLabel() {
            return resource.getValueMap()
                    .get("textareaLabel", String.class);
        }

        public String getTextareaPlaceholder() {
            return resource.getValueMap()
                    .get("textareaPlaceholder", String.class);
        }

        public String getButtonText() {
            return resource.getValueMap()
                    .get("buttonText", String.class);
        }
    }
}