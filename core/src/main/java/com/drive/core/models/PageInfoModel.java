package com.drive.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PageInfoModel {

    @Self
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private PageManager pageManager;

    private Page currentPage;

    private Page parentPage;

    private Page requestedPage;

    private Resource retrievedResource;

    private Resource parentResource;

    private Resource contentResource;

    private String resourcePath;
    private String resourceName;
    private String resourceType;

    private String retrievedResourcePath;
    private String retrievedResourceName;
    private String resourceStatus;

    private String pagePath;
    private String pageName;
    private String pageTitle;
    private String parentPagePath;

    private String jcrTitle;
    private String jcrDescription;

    private int childPageCount;
    private int pageDepth;

    private String parentResourcePath;
    private String contentResourcePath;

    private String requestedPageTitle;


    @PostConstruct
    protected void init() {


        pageManager = resourceResolver.adaptTo(PageManager.class);

        if (resource != null) {

            resourcePath = resource.getPath();

            resourceName = resource.getName();

            resourceType = resource.getResourceType();


            parentResource = resource.getParent();

            if (parentResource != null) {
                parentResourcePath = parentResource.getPath();
            } else {
                parentResourcePath = "Parent Resource Not Found";
            }
        }


        String testResourcePath =
                "/content/drive/us/en/service_page";

        retrievedResource =
                resourceResolver.getResource(testResourcePath);

        if (retrievedResource != null) {

            retrievedResourcePath =
                    retrievedResource.getPath();

            retrievedResourceName =
                    retrievedResource.getName();

            resourceStatus = "Resource Found";

        } else {

            retrievedResourcePath =
                    "Resource Not Found";

            retrievedResourceName =
                    "Resource Not Found";

            resourceStatus =
                    "Resource Not Found";
        }



        if (pageManager != null && resource != null) {

            currentPage =
                    pageManager.getContainingPage(resource);
        }



        if (currentPage != null) {

            pagePath =
                    currentPage.getPath();

            pageName =
                    currentPage.getName();

            pageTitle =
                    currentPage.getTitle();

            pageDepth =
                    currentPage.getDepth();


            parentPage =
                    currentPage.getParent();

            if (parentPage != null) {

                parentPagePath =
                        parentPage.getPath();

            } else {

                parentPagePath =
                        "Parent Page Not Found";
            }




            childPageCount = 0;

            if (currentPage.listChildren() != null) {

                while (currentPage.listChildren().hasNext()) {

                    currentPage.listChildren().next();

                    childPageCount++;
                }
            }



            contentResource =
                    currentPage.getContentResource();

            if (contentResource != null) {

                contentResourcePath =
                        contentResource.getPath();



                ValueMap properties =
                        contentResource.getValueMap();

                jcrTitle =
                        properties.get(
                                "jcr:title",
                                String.class
                        );

                jcrDescription =
                        properties.get(
                                "jcr:description",
                                String.class
                        );


                if (jcrTitle == null) {
                    jcrTitle = "No Title";
                }

                if (jcrDescription == null) {
                    jcrDescription = "No Description";
                }

            } else {

                contentResourcePath =
                        "jcr:content Resource Not Found";

                jcrTitle =
                        "No Title";

                jcrDescription =
                        "No Description";
            }

        } else {

            pagePath = "Page Not Found";
            pageName = "Page Not Found";
            pageTitle = "Page Not Found";
            parentPagePath = "Page Not Found";
            pageDepth = 0;
            childPageCount = 0;
            contentResourcePath = "Page Not Found";
            jcrTitle = "Page Not Found";
            jcrDescription = "Page Not Found";
        }



        if (pageManager != null) {

            requestedPage =
                    pageManager.getPage(
                            "/content/drive/us/en/service_page"
                    );

            if (requestedPage != null) {

                requestedPageTitle =
                        requestedPage.getTitle();

            } else {

                requestedPageTitle =
                        "Page Not Found";
            }
        }
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getRetrievedResourcePath() {
        return retrievedResourcePath;
    }

    public String getRetrievedResourceName() {
        return retrievedResourceName;
    }

    public String getResourceStatus() {
        return resourceStatus;
    }

    public String getPagePath() {
        return pagePath;
    }

    public String getPageName() {
        return pageName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getParentPagePath() {
        return parentPagePath;
    }

    public String getJcrTitle() {
        return jcrTitle;
    }

    public String getJcrDescription() {
        return jcrDescription;
    }

    public int getChildPageCount() {
        return childPageCount;
    }

    public String getParentResourcePath() {
        return parentResourcePath;
    }

    public String getContentResourcePath() {
        return contentResourcePath;
    }

    public int getPageDepth() {
        return pageDepth;
    }

    public String getRequestedPageTitle() {
        return requestedPageTitle;
    }
}