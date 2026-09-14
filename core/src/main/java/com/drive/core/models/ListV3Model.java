package com.drive.core.models;

import com.day.cq.search.Predicate;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ListV3Model {

    private static final Logger LOG =
            LoggerFactory.getLogger(ListV3Model.class);

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

    @ValueMapValue
    private boolean displayItemAsTeaser;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ScriptVariable
    private Page currentPage;

    @OSGiService
    private QueryBuilder queryBuilder;

    private final List<Page> pageItems =
            new ArrayList<>();

    private final List<ListV3ItemModel> items =
            new ArrayList<>();

    private final Set<String> addedPagePaths =
            new LinkedHashSet<>();

    private boolean searchExecuted = false;

    @PostConstruct
    protected void init() {

        if (listFrom == null
                || listFrom.trim().isEmpty()) {

            LOG.debug("List V3 source is not configured");
            return;
        }

        String source = listFrom.trim();

        switch (source) {

            case "children":
                buildFromChildren();
                break;

            case "static":
                buildFromStaticList();
                break;

            case "search":

                if (query != null
                        && !query.trim().isEmpty()) {

                    searchExecuted = true;
                    buildFromSearch();

                } else {

                    LOG.debug(
                            "Search selected but search query is empty"
                    );
                }

                break;

            case "tags":
                buildFromTags();
                break;

            default:

                LOG.warn(
                        "Unknown List V3 source: {}",
                        listFrom
                );

                return;
        }

        sortItems();
        applyMaxItems();
        createItemModels();

        LOG.info(
                "List V3 completed. Source: {}, Items: {}",
                source,
                items.size()
        );
    }

    private void addPageIfNotDuplicate(Page page) {

        if (page == null) {
            return;
        }

        String pagePath = page.getPath();

        if (pagePath == null
                || pagePath.trim().isEmpty()) {

            return;
        }

        if (addedPagePaths.add(pagePath)) {

            pageItems.add(page);

            LOG.debug(
                    "Page added to List V3: {}",
                    pagePath
            );

        } else {

            LOG.debug(
                    "Duplicate page skipped from List V3: {}",
                    pagePath
            );
        }
    }

    private void buildFromChildren() {

        if (resourceResolver == null) {

            LOG.warn("ResourceResolver is unavailable");
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {

            LOG.warn("PageManager could not be adapted");
            return;
        }

        Page parent;

        if (parentPage == null
                || parentPage.trim().isEmpty()) {

            parent = currentPage;

        } else {

            parent = pageManager.getPage(
                    parentPage.trim()
            );
        }

        if (parent == null) {

            LOG.warn(
                    "Parent page could not be found: {}",
                    parentPage
            );

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

        if (parent == null
                || depth <= 0) {

            return;
        }

        Iterator<Page> children =
                parent.listChildren();

        while (children.hasNext()) {

            Page child = children.next();

            if (child == null) {
                continue;
            }

            addPageIfNotDuplicate(child);

            if (depth > 1) {

                collectChildren(
                        child,
                        depth - 1
                );
            }
        }
    }

    private void buildFromStaticList() {

        if (pages == null
                || pages.length == 0) {

            LOG.debug(
                    "No fixed-list pages configured"
            );

            return;
        }

        if (resourceResolver == null) {

            LOG.warn("ResourceResolver is unavailable");
            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {

            LOG.warn("PageManager could not be adapted");
            return;
        }

        for (String pagePath : pages) {

            if (pagePath == null
                    || pagePath.trim().isEmpty()) {

                continue;
            }

            Page page =
                    pageManager.getPage(
                            pagePath.trim()
                    );

            if (page == null) {

                LOG.warn(
                        "Fixed-list page not found: {}",
                        pagePath
                );

                continue;
            }

            addPageIfNotDuplicate(page);
        }
    }

    private void buildFromSearch() {

        if (query == null
                || query.trim().isEmpty()) {

            LOG.debug("Search query is empty");
            return;
        }

        if (queryBuilder == null) {

            LOG.warn("QueryBuilder service is unavailable");
            return;
        }

        if (resourceResolver == null) {

            LOG.warn("ResourceResolver is unavailable");
            return;
        }

        Session session =
                resourceResolver.adaptTo(Session.class);

        if (session == null) {

            LOG.warn(
                    "Could not adapt ResourceResolver to Session"
            );

            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {

            LOG.warn("PageManager could not be adapted");
            return;
        }

        String searchRoot = searchIn;

        if (searchRoot == null
                || searchRoot.trim().isEmpty()) {

            if (currentPage == null) {

                LOG.warn(
                        "Search root and current page unavailable"
                );

                return;
            }

            searchRoot = currentPage.getPath();
        }

        PredicateGroup predicates =
                new PredicateGroup();

        predicates.add(
                new Predicate("path")
                        .set(
                                "path",
                                searchRoot.trim()
                        )
        );

        predicates.add(
                new Predicate("type")
                        .set(
                                "type",
                                "cq:Page"
                        )
        );

        predicates.add(
                new Predicate("fulltext")
                        .set(
                                "fulltext",
                                query.trim()
                        )
        );

        predicates.add(
                new Predicate("p.limit")
                        .set(
                                "p.limit",
                                "-1"
                        )
        );

        try {

            Query searchQuery =
                    queryBuilder.createQuery(
                            predicates,
                            session
                    );

            SearchResult searchResult =
                    searchQuery.getResult();

            for (Hit hit : searchResult.getHits()) {

                String hitPath =
                        hit.getPath();

                if (hitPath == null
                        || hitPath.trim().isEmpty()) {

                    continue;
                }

                Page page =
                        pageManager.getPage(hitPath);

                if (page == null) {
                    continue;
                }

                addPageIfNotDuplicate(page);
            }

        } catch (Exception e) {

            LOG.error(
                    "Error executing List V3 full-text search",
                    e
            );
        }
    }

    private void buildFromTags() {

        if (tags == null
                || tags.length == 0) {

            LOG.debug(
                    "No tags configured for List V3"
            );

            return;
        }

        if (queryBuilder == null) {

            LOG.warn("QueryBuilder service is unavailable");
            return;
        }

        if (resourceResolver == null) {

            LOG.warn("ResourceResolver is unavailable");
            return;
        }

        Session session =
                resourceResolver.adaptTo(Session.class);

        if (session == null) {

            LOG.warn(
                    "Could not adapt ResourceResolver to Session"
            );

            return;
        }

        PageManager pageManager =
                resourceResolver.adaptTo(PageManager.class);

        if (pageManager == null) {

            LOG.warn("PageManager could not be adapted");
            return;
        }

        String rootPath = tagsSearchRoot;

        if (rootPath == null
                || rootPath.trim().isEmpty()) {

            if (currentPage == null) {

                LOG.warn(
                        "Tags root and current page unavailable"
                );

                return;
            }

            rootPath = currentPage.getPath();
        }

        PredicateGroup predicates =
                new PredicateGroup();

        predicates.add(
                new Predicate("path")
                        .set(
                                "path",
                                rootPath.trim()
                        )
        );

        predicates.add(
                new Predicate("type")
                        .set(
                                "type",
                                "cq:PageContent"
                        )
        );

        predicates.add(
                new Predicate("p.limit")
                        .set(
                                "p.limit",
                                "-1"
                        )
        );

        try {

            Query tagQuery =
                    queryBuilder.createQuery(
                            predicates,
                            session
                    );

            SearchResult searchResult =
                    tagQuery.getResult();

            for (Hit hit : searchResult.getHits()) {

                String contentPath =
                        hit.getPath();

                if (contentPath == null
                        || contentPath.trim().isEmpty()) {

                    continue;
                }

                Resource contentResource =
                        resourceResolver.getResource(
                                contentPath
                        );

                if (contentResource == null) {
                    continue;
                }

                String[] pageTags =
                        contentResource
                                .getValueMap()
                                .get(
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

                if (page == null) {
                    continue;
                }

                addPageIfNotDuplicate(page);
            }

        } catch (Exception e) {

            LOG.error(
                    "Error executing List V3 tag search",
                    e
            );
        }
    }

    private boolean matchesTags(String[] pageTags) {

        if (tags == null
                || tags.length == 0) {

            return false;
        }

        if ("all".equalsIgnoreCase(tagsMatch)) {

            boolean validTagFound = false;

            for (String selectedTag : tags) {

                if (selectedTag == null
                        || selectedTag.trim().isEmpty()) {

                    continue;
                }

                validTagFound = true;

                if (!containsTag(
                        pageTags,
                        selectedTag.trim()
                )) {

                    return false;
                }
            }

            return validTagFound;
        }

        for (String selectedTag : tags) {

            if (selectedTag == null
                    || selectedTag.trim().isEmpty()) {

                continue;
            }

            if (containsTag(
                    pageTags,
                    selectedTag.trim()
            )) {

                return true;
            }
        }

        return false;
    }

    private boolean containsTag(
            String[] pageTags,
            String selectedTag) {

        if (pageTags == null
                || selectedTag == null) {

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

        if (orderBy == null
                || orderBy.trim().isEmpty()) {

            return;
        }

        Comparator<Page> comparator;

        if ("title".equalsIgnoreCase(orderBy)) {

            comparator =
                    Comparator.comparing(
                            this::getDisplayTitle,
                            String.CASE_INSENSITIVE_ORDER
                    );

        } else if ("modified".equalsIgnoreCase(orderBy)) {

            comparator =
                    Comparator.comparing(
                            this::getModifiedDate
                    );

        } else {

            LOG.warn(
                    "Unsupported orderBy value: {}",
                    orderBy
            );

            return;
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {

            comparator =
                    comparator.reversed();
        }

        pageItems.sort(comparator);
    }

    private String getDisplayTitle(Page page) {

        if (page == null) {
            return "";
        }

        if (page.getTitle() != null
                && !page.getTitle().trim().isEmpty()) {

            return page.getTitle();
        }

        if (page.getNavigationTitle() != null
                && !page.getNavigationTitle().trim().isEmpty()) {

            return page.getNavigationTitle();
        }

        if (page.getName() != null
                && !page.getName().trim().isEmpty()) {

            return page.getName();
        }

        return "";
    }

    private Calendar getModifiedDate(Page page) {

        if (page != null
                && page.getLastModified() != null) {

            return page.getLastModified();
        }

        Calendar defaultDate =
                Calendar.getInstance();

        defaultDate.clear();

        defaultDate.set(
                1970,
                Calendar.JANUARY,
                1
        );

        return defaultDate;
    }

    private void applyMaxItems() {

        if (maxItems <= 0) {
            return;
        }

        if (pageItems.size() <= maxItems) {
            return;
        }

        pageItems.subList(
                maxItems,
                pageItems.size()
        ).clear();
    }

    private void createItemModels() {

        items.clear();

        for (Page page : pageItems) {

            if (page == null) {
                continue;
            }

            items.add(
                    new ListV3ItemModel(page)
            );
        }
    }

    public boolean isEmpty() {

        return searchExecuted
                && items.isEmpty();
    }

    public int getItemCount() {

        return items.size();
    }

    public List<ListV3ItemModel> getItems() {

        return Collections.unmodifiableList(items);
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

    public String[] getPages() {

        return pages;
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
}