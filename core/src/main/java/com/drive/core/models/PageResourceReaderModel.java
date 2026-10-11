package com.drive.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PageResourceReaderModel {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String pagePath;

    private String pageTitle;
    private String pageDescription;

    // Components on the selected page
    private List<ComponentInfo> components = new ArrayList<>();

    // Child pages and their details
    private List<ChildPageInfo> childPages = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if (pagePath == null || pagePath.trim().isEmpty()) {
            return;
        }

        PageManager pageManager =
                request.getResourceResolver().adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        // Get the selected page
        Page page = pageManager.getPage(pagePath);

        if (page == null) {
            return;
        }

        // Selected page details
        pageTitle = page.getTitle();
        pageDescription = page.getDescription();

        // Read components on the selected page
        Resource contentResource = page.getContentResource();

        if (contentResource != null) {
            Resource root = contentResource.getChild("root");

            if (root != null) {
                readComponents(root, components);
            }
        }

        // Read child pages and their components
        readChildPages(page);
    }

    // Read components recursively into the supplied list
    private void readComponents(
            Resource parent,
            List<ComponentInfo> targetComponents) {

        Iterator<Resource> children = parent.listChildren();

        while (children.hasNext()) {

            Resource child = children.next();

            String resourceType =
                    child.getValueMap().get(
                            "sling:resourceType", String.class);

            if (resourceType != null) {

                ComponentInfo info = new ComponentInfo();

                info.setName(child.getName());
                info.setPath(child.getPath());
                info.setResourceType(resourceType);

                List<PropertyInfo> properties = new ArrayList<>();
                ValueMap valueMap = child.getValueMap();

                for (Map.Entry<String, Object> entry
                        : valueMap.entrySet()) {

                    String key = entry.getKey();
                    Object value = entry.getValue();

                    // Skip repository metadata
                    if (key.startsWith("jcr:")
                            || key.startsWith("sling:")
                            || key.startsWith("cq:")) {
                        continue;
                    }

                    if (value == null) {
                        continue;
                    }

                    PropertyInfo property = new PropertyInfo();
                    property.setName(key);

                    if (value instanceof Calendar) {

                        property.setValue(
                                formatDateTime((Calendar) value));

                    } else if (value instanceof Calendar[]) {

                        Calendar[] dates = (Calendar[]) value;
                        List<String> formattedDates = new ArrayList<>();

                        for (Calendar date : dates) {
                            formattedDates.add(
                                    date == null
                                            ? ""
                                            : formatDateTime(date));
                        }

                        property.setValue(formattedDates.toString());

                    } else if (value instanceof Object[]) {

                        property.setValue(
                                Arrays.deepToString((Object[]) value));

                    } else if (value instanceof boolean[]
                            || value instanceof byte[]
                            || value instanceof short[]
                            || value instanceof int[]
                            || value instanceof long[]
                            || value instanceof float[]
                            || value instanceof double[]
                            || value instanceof char[]) {

                        property.setValue(arrayToString(value));

                    } else {

                        property.setValue(String.valueOf(value));
                    }

                    properties.add(property);
                }

                info.setProperties(properties);
                targetComponents.add(info);
            }

            // Read nested component containers
            if (child.hasChildren()) {
                readComponents(child, targetComponents);
            }
        }
    }

    // Read direct child pages, then their descendants
    private void readChildPages(Page parentPage) {

        Iterator<Page> children = parentPage.listChildren();

        while (children.hasNext()) {

            Page childPage = children.next();

            ChildPageInfo info = new ChildPageInfo();

            info.setPath(childPage.getPath());
            info.setTitle(childPage.getTitle());
            info.setDescription(childPage.getDescription());

            // Read components belonging to this child page
            List<ComponentInfo> childComponents = new ArrayList<>();

            Resource content = childPage.getContentResource();

            if (content != null) {

                Resource root = content.getChild("root");

                if (root != null) {
                    readComponents(root, childComponents);
                }
            }

            info.setComponents(childComponents);
            childPages.add(info);

            // Read any pages nested beneath this child page
            readChildPages(childPage);
        }
    }

    // Format a date using India time
    private String formatDateTime(Calendar calendar) {

        SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy HH:mm");

        formatter.setTimeZone(
                TimeZone.getTimeZone("Asia/Kolkata"));

        return formatter.format(calendar.getTime());
    }

    // Convert primitive arrays into readable strings
    private String arrayToString(Object value) {

        if (value instanceof boolean[]) {
            return Arrays.toString((boolean[]) value);
        } else if (value instanceof byte[]) {
            return Arrays.toString((byte[]) value);
        } else if (value instanceof short[]) {
            return Arrays.toString((short[]) value);
        } else if (value instanceof int[]) {
            return Arrays.toString((int[]) value);
        } else if (value instanceof long[]) {
            return Arrays.toString((long[]) value);
        } else if (value instanceof float[]) {
            return Arrays.toString((float[]) value);
        } else if (value instanceof double[]) {
            return Arrays.toString((double[]) value);
        } else if (value instanceof char[]) {
            return Arrays.toString((char[]) value);
        }

        return String.valueOf(value);
    }

    // Selected page getters
    public String getPagePath() {
        return pagePath;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public List<ComponentInfo> getComponents() {
        return Collections.unmodifiableList(components);
    }

    // Child page getter
    public List<ChildPageInfo> getChildPages() {
        return Collections.unmodifiableList(childPages);
    }

    // Component details
    public static class ComponentInfo {

        private String name;
        private String path;
        private String resourceType;
        private List<PropertyInfo> properties = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public List<PropertyInfo> getProperties() {
            return properties;
        }

        public void setProperties(List<PropertyInfo> properties) {
            this.properties = properties;
        }
    }

    // Component property details
    public static class PropertyInfo {

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    // Child page details
    public static class ChildPageInfo {

        private String path;
        private String title;
        private String description;

        private List<ComponentInfo> components = new ArrayList<>();

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<ComponentInfo> getComponents() {
            return Collections.unmodifiableList(components);
        }

        public void setComponents(List<ComponentInfo> components) {
            this.components = components;
        }
    }
}

