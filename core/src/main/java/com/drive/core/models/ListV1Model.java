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
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ListV1Model {

    @ValueMapValue
    private String listFrom;

    @ValueMapValue
    private String parentPage;

    @ValueMapValue
    private int childDepth;

    @ValueMapValue
    private String[] pages;

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

    @SlingObject
    private ResourceResolver resourceResolver;

    @ScriptVariable
    private Page currentPage;

    @OSGiService
    private QueryBuilder queryBuilder;

    private List<Page> items = new ArrayList<>();

    @PostConstruct
    protected void init() {

        if ("children".equals(listFrom)) {

            buildFromChildren();

        } else if ("static".equals(listFrom)) {

            buildStaticList();

        } else if ("search".equals(listFrom)) {

            buildFromSearch();

        } else if ("tags".equals(listFrom)) {

            buildFromTags();
        }

        sortItems();

        applyMaxItems();
    }

    private void buildFromChildren() {

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        Page parent;

        if (parentPage == null || parentPage.isEmpty()) {

            parent = currentPage;

        } else {

            parent = pageManager.getPage(parentPage);
        }

        if (parent == null) {
            return;
        }

        int depth = childDepth > 0
                ? childDepth
                : 1;

        collectChildren(parent, depth);
    }

    private void collectChildren(
            Page parent,
            int depth) {

        if (depth <= 0) {
            return;
        }

        Iterator<Page> children =
                parent.listChildren();

        while (children.hasNext()) {

            Page child = children.next();

            items.add(child);

            if (depth > 1) {

                collectChildren(
                        child,
                        depth - 1
                );
            }
        }
    }

    private void buildStaticList() {

        if (pages == null || pages.length == 0) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        for (String path : pages) {

            if (path == null || path.isEmpty()) {
                continue;
            }

            Page page =
                    pageManager.getPage(path);

            if (page != null) {

                items.add(page);
            }
        }
    }

    private void buildFromSearch() {

        if (query == null || query.isEmpty()) {
            return;
        }

        if (searchIn == null || searchIn.isEmpty()) {
            return;
        }

        if (queryBuilder == null) {
            return;
        }

        try {

            Session session =
                    resourceResolver.adaptTo(Session.class);

            if (session == null) {
                return;
            }

            Map<String, String> predicates =
                    new HashMap<>();

            predicates.put(
                    "path",
                    searchIn
            );

            predicates.put(
                    "type",
                    "cq:Page"
            );

            predicates.put(
                    "fulltext",
                    query
            );

            predicates.put(
                    "p.limit",
                    "-1"
            );

            Query searchQuery =
                    queryBuilder.createQuery(
                            PredicateGroup.create(predicates),
                            session
                    );

            SearchResult searchResult =
                    searchQuery.getResult();

            PageManager pageManager =
                    resourceResolver.adaptTo(PageManager.class);

            if (pageManager == null) {
                return;
            }

            for (Hit hit : searchResult.getHits()) {

                String path =
                        hit.getPath();

                Page page =
                        pageManager.getPage(path);

                if (page != null) {

                    items.add(page);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void buildFromTags() {

        if (tags == null || tags.length == 0) {
            return;
        }

        if (queryBuilder == null) {
            return;
        }

        Session session =
                resourceResolver.adaptTo(Session.class);

        if (session == null) {
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {
            return;
        }

        String rootPath;

        if (tagsSearchRoot == null
                || tagsSearchRoot.isEmpty()) {

            if (currentPage == null) {
                return;
            }

            rootPath = currentPage.getPath();

        } else {

            rootPath = tagsSearchRoot;
        }

        try {

            Map<String, String> predicates =
                    new HashMap<>();

            predicates.put(
                    "path",
                    rootPath
            );

            predicates.put(
                    "type",
                    "cq:PageContent"
            );

            predicates.put(
                    "p.limit",
                    "-1"
            );

            Query tagQuery =
                    queryBuilder.createQuery(
                            PredicateGroup.create(predicates),
                            session
                    );

            SearchResult searchResult =
                    tagQuery.getResult();

            for (Hit hit : searchResult.getHits()) {

                Resource contentResource =
                        resourceResolver.getResource(
                                hit.getPath()
                        );

                if (contentResource == null) {
                    continue;
                }

                String[] pageTags =
                        contentResource.getValueMap().get(
                                "cq:tags",
                                String[].class
                        );

                if (pageTags == null
                        || pageTags.length == 0) {

                    continue;
                }

                if (!matchesTags(pageTags)) {
                    continue;
                }

                Page page =
                        pageManager.getContainingPage(
                                contentResource
                        );

                if (page != null) {

                    items.add(page);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private boolean matchesTags(String[] pageTags) {

        if ("all".equalsIgnoreCase(tagsMatch)) {

            for (String selectedTag : tags) {

                if (!containsTag(
                        pageTags,
                        selectedTag)) {

                    return false;
                }
            }

            return true;
        }

        for (String selectedTag : tags) {

            if (containsTag(
                    pageTags,
                    selectedTag)) {

                return true;
            }
        }

        return false;
    }

    private boolean containsTag(
            String[] pageTags,
            String selectedTag) {

        if (selectedTag == null
                || selectedTag.isEmpty()) {

            return false;
        }

        for (String pageTag : pageTags) {

            if (selectedTag.equals(pageTag)) {

                return true;
            }
        }

        return false;
    }

    private void sortItems() {

        if (orderBy == null || orderBy.isEmpty()) {
            return;
        }

        Comparator<Page> comparator;

        if ("title".equals(orderBy)) {

            comparator = Comparator.comparing(
                    page -> page.getTitle() != null
                            ? page.getTitle()
                            : "",
                    String.CASE_INSENSITIVE_ORDER
            );

        } else if ("modified".equals(orderBy)) {

            comparator = Comparator.comparing(
                    page -> page.getLastModified() != null
                            ? page.getLastModified()
                            : new GregorianCalendar(
                            1970,
                            0,
                            1
                    )
            );

        } else {

            return;
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {

            comparator =
                    comparator.reversed();
        }

        items.sort(comparator);
    }

    private void applyMaxItems() {

        if (maxItems <= 0) {
            return;
        }

        if (items.size() <= maxItems) {
            return;
        }

        items = new ArrayList<>(
                items.subList(
                        0,
                        maxItems
                )
        );
    }

    public List<Page> getItems() {

        return Collections.unmodifiableList(items);
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

    public String getId() {

        return id;
    }
}