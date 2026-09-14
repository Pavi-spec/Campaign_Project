package com.drive.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeroReferenceModel {

    @ValueMapValue
    private String heroPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Resource heroResource;

    @PostConstruct
    protected void init() {

        if (heroPage == null || heroPage.isEmpty()) {
            return;
        }

        if (resourceResolver == null) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        Page page = pageManager.getPage(heroPage);

        if (page == null) {
            return;
        }

        Resource contentResource = page.getContentResource();

        if (contentResource == null) {
            return;
        }

        Resource rootResource = contentResource.getChild("root");

        if (rootResource == null) {
            return;
        }

        heroResource = findHeroResource(rootResource);
    }

    private Resource findHeroResource(Resource resource) {

        if ("drive/components/hero".equals(resource.getResourceType())) {
            return resource;
        }

        for (Resource child : resource.getChildren()) {

            Resource result = findHeroResource(child);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    public Resource getHeroResource() {
        return heroResource;
    }

    public String getHeroPage() {
        return heroPage;
    }
}