package com.drive.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ListV4Model {

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private QueryBuilder queryBuilder;

    @ValueMapValue
    private String listFrom;

    @ValueMapValue
    private String parentPage;

    @ValueMapValue
    private int childDepth;

    @ValueMapValue
    private String query;

    @ValueMapValue
    private String searchIn;

    @ValueMapValue
    private String tagsSearchRoot;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private String tagsMatch;

    @ValueMapValue
    private String orderBy;

    @ValueMapValue
    private String sortOrder;

    @ValueMapValue
    private int maxItems;

    @ValueMapValue
    private String id;

    @ValueMapValue
    private boolean linkItems;

    @ValueMapValue
    private boolean showDescription;

    @ValueMapValue
    private boolean showModificationDate;

    @ValueMapValue
    private boolean displayItemAsTeaser;

    @ChildResource(name = "static")
    private List<StaticListItem> staticItems;

    private final List<ListV4ItemModel> items =
            new ArrayList<>();

    private boolean searchExecuted;

    @PostConstruct
    protected void init() {

        if (listFrom == null || listFrom.trim().isEmpty()) {
            listFrom = "children";
        }

        switch (listFrom) {

            case "children":
                collectChildPages();
                break;

            case "static":
                collectStaticItems();
                break;

            case "search":
                collectSearchResults();
                break;

            case "tags":
                collectTagResults();
                break;

            default:
                break;
        }

        applySorting();
        applyMaxItems();
    }

    private void collectChildPages() {

        if (resourceResolver == null || resource == null) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        String rootPath = parentPage;

        if (rootPath == null || rootPath.trim().isEmpty()) {

            Page currentPage =
                    pageManager.getContainingPage(resource);

            if (currentPage != null) {
                rootPath = currentPage.getPath();
            }
        }

        if (rootPath == null || rootPath.trim().isEmpty()) {
            return;
        }

        Page rootPage =
                pageManager.getPage(rootPath);

        if (rootPage == null) {
            return;
        }

        Set<String> visitedPaths =
                new LinkedHashSet<>();

        if (childDepth <= 0) {

            collectDirectChildren(
                    rootPage,
                    visitedPaths
            );

        } else {

            collectChildrenRecursively(
                    rootPage,
                    childDepth,
                    visitedPaths
            );
        }
    }

    private void collectDirectChildren(
            Page rootPage,
            Set<String> visitedPaths
    ) {

        Iterator<Page> children =
                rootPage.listChildren();

        while (children.hasNext()) {

            Page childPage = children.next();

            addPageItem(
                    childPage,
                    visitedPaths
            );
        }
    }

    private void collectChildrenRecursively(
            Page rootPage,
            int depth,
            Set<String> visitedPaths
    ) {

        if (depth <= 0) {
            return;
        }

        Iterator<Page> children =
                rootPage.listChildren();

        while (children.hasNext()) {

            Page childPage = children.next();

            addPageItem(
                    childPage,
                    visitedPaths
            );

            collectChildrenRecursively(
                    childPage,
                    depth - 1,
                    visitedPaths
            );
        }
    }

    private void addPageItem(
            Page page,
            Set<String> visitedPaths
    ) {

        if (page == null) {
            return;
        }

        String pagePath = page.getPath();

        if (visitedPaths.contains(pagePath)) {
            return;
        }

        visitedPaths.add(pagePath);

        items.add(
                new ListV4ItemModel(
                        page,
                        linkItems
                )
        );
    }

    private void collectStaticItems() {

        if (staticItems == null || staticItems.isEmpty()) {
            return;
        }

        for (StaticListItem staticItem : staticItems) {

            if (staticItem == null) {
                continue;
            }

            String linkURL = staticItem.getLinkURL();
            String linkText = staticItem.getLinkText();
            String linkTarget = staticItem.getLinkTarget();

            boolean emptyURL =
                    linkURL == null || linkURL.trim().isEmpty();

            boolean emptyText =
                    linkText == null || linkText.trim().isEmpty();

            if (emptyURL && emptyText) {
                continue;
            }

            items.add(
                    new ListV4ItemModel(
                            linkURL,
                            linkText,
                            linkTarget
                    )
            );
        }
    }

    private void collectSearchResults() {

        searchExecuted = true;

        if (queryBuilder == null
                || resourceResolver == null
                || query == null
                || query.trim().isEmpty()) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        String searchRoot = searchIn;

        if (searchRoot == null || searchRoot.trim().isEmpty()) {

            Page currentPage =
                    pageManager.getContainingPage(resource);

            if (currentPage != null) {
                searchRoot = currentPage.getPath();
            }
        }

        if (searchRoot == null || searchRoot.trim().isEmpty()) {
            return;
        }

        Session session = getSession();

        if (session == null) {
            return;
        }

        Map<String, String> predicates =
                new HashMap<>();

        predicates.put("path", searchRoot);
        predicates.put("type", "cq:Page");
        predicates.put("fulltext", query.trim());
        predicates.put("p.limit", "-1");

        Query searchQuery =
                queryBuilder.createQuery(
                        PredicateGroup.create(predicates),
                        session
                );

        SearchResult searchResult =
                searchQuery.getResult();

        Set<String> addedPaths =
                new HashSet<>();

        for (Hit hit : searchResult.getHits()) {

            try {

                String pagePath = hit.getPath();

                if (pagePath == null
                        || addedPaths.contains(pagePath)) {
                    continue;
                }

                Page page =
                        pageManager.getPage(pagePath);

                if (page == null) {
                    continue;
                }

                items.add(
                        new ListV4ItemModel(
                                page,
                                linkItems
                        )
                );

                addedPaths.add(pagePath);

            } catch (Exception exception) {
                continue;
            }
        }
    }

    private void collectTagResults() {

        searchExecuted = true;

        if (queryBuilder == null
                || resourceResolver == null
                || tags == null
                || tags.length == 0) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        String rootPath = tagsSearchRoot;

        if (rootPath == null || rootPath.trim().isEmpty()) {

            Page currentPage =
                    pageManager.getContainingPage(resource);

            if (currentPage != null) {
                rootPath = currentPage.getPath();
            }
        }

        if (rootPath == null || rootPath.trim().isEmpty()) {
            return;
        }

        Session session = getSession();

        if (session == null) {
            return;
        }

        Map<String, String> predicates =
                new HashMap<>();

        predicates.put("path", rootPath);
        predicates.put("type", "cq:PageContent");
        predicates.put("property", "cq:tags");
        predicates.put("property.value", tags[0]);
        predicates.put("p.limit", "-1");

        Query tagQuery =
                queryBuilder.createQuery(
                        PredicateGroup.create(predicates),
                        session
                );

        SearchResult searchResult =
                tagQuery.getResult();

        Set<String> addedPaths =
                new HashSet<>();

        for (Hit hit : searchResult.getHits()) {

            try {

                Resource contentResource =
                        resourceResolver.getResource(
                                hit.getPath()
                        );

                if (contentResource == null) {
                    continue;
                }

                Page page =
                        pageManager.getContainingPage(
                                contentResource
                        );

                if (page == null) {
                    continue;
                }

                String pagePath = page.getPath();

                if (addedPaths.contains(pagePath)) {
                    continue;
                }

                if (!matchesTags(contentResource)) {
                    continue;
                }

                items.add(
                        new ListV4ItemModel(
                                page,
                                linkItems
                        )
                );

                addedPaths.add(pagePath);

            } catch (Exception exception) {
                continue;
            }
        }
    }

    private boolean matchesTags(
            Resource contentResource
    ) {

        if (tags == null || tags.length == 0) {
            return false;
        }

        String[] pageTags =
                contentResource.getValueMap().get(
                        "cq:tags",
                        String[].class
                );

        if (pageTags == null || pageTags.length == 0) {
            return false;
        }

        boolean matchAll =
                "all".equalsIgnoreCase(tagsMatch);

        if (matchAll) {

            for (String requiredTag : tags) {

                boolean found = false;

                for (String pageTag : pageTags) {

                    if (requiredTag.equals(pageTag)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return true;
        }

        for (String requiredTag : tags) {

            for (String pageTag : pageTags) {

                if (requiredTag.equals(pageTag)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Session getSession() {

        if (resourceResolver == null) {
            return null;
        }

        return resourceResolver.adaptTo(Session.class);
    }

    private void applySorting() {

        if (items.isEmpty()) {
            return;
        }

        if ("modified".equalsIgnoreCase(orderBy)) {

            items.sort(
                    Comparator.comparing(
                            ListV4ItemModel::getLastModified,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder()
                            )
                    )
            );

        } else {

            items.sort(
                    Comparator.comparing(
                            item -> item.getDisplayTitle() == null
                                    ? ""
                                    : item.getDisplayTitle()
                                    .toLowerCase()
                    )
            );
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            Collections.reverse(items);
        }
    }

    private void applyMaxItems() {

        if (maxItems <= 0
                || items.size() <= maxItems) {
            return;
        }

        List<ListV4ItemModel> limitedItems =
                new ArrayList<>(
                        items.subList(0, maxItems)
                );

        items.clear();
        items.addAll(limitedItems);
    }

    public List<ListV4ItemModel> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isSearchExecuted() {
        return searchExecuted;
    }

    public String getListFrom() {
        return listFrom;
    }

    public String getParentPage() {
        return parentPage;
    }

    public int getChildDepth() {
        return childDepth;
    }

    public String getQuery() {
        return query;
    }

    public String getSearchIn() {
        return searchIn;
    }

    public String getTagsSearchRoot() {
        return tagsSearchRoot;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTagsMatch() {
        return tagsMatch;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public String getId() {
        return id;
    }

    public boolean isLinkItems() {
        return linkItems;
    }

    public boolean isShowDescription() {
        return showDescription;
    }

    public boolean isShowModificationDate() {
        return showModificationDate;
    }

    public boolean isDisplayItemAsTeaser() {
        return displayItemAsTeaser;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class StaticListItem {

        @ValueMapValue
        private String linkURL;

        @ValueMapValue
        private String linkTarget;

        @ValueMapValue
        private String linkText;

        public String getLinkURL() {
            return linkURL;
        }

        public String getLinkTarget() {
            return linkTarget;
        }

        public String getLinkText() {
            return linkText;
        }
    }
}