package com.drive.core.services.impl;

import com.drive.core.services.SearchService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(service = SearchService.class, immediate = true)
public class SearchServiceImpl implements SearchService {

    private static final Logger LOG =
            LoggerFactory.getLogger(SearchServiceImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private Map<String, String> createTextSearchQuery(
            String searchText,
            int startResult,
            int resultPerPage) {

        Map<String, String> queryMap = new HashMap<>();

        queryMap.put("path", "/content/drive");
        queryMap.put("type", "cq:Page");
        queryMap.put("fulltext", searchText);
        queryMap.put("p.offset", String.valueOf(startResult));
        queryMap.put("p.limit", String.valueOf(resultPerPage));

        return queryMap;
    }

    @Override
    public JSONObject searchResult(
            String searchText,
            int startResult,
            int resultPerPage) {

        JSONObject response = new JSONObject();

        if (searchText == null || searchText.trim().isEmpty()
                || startResult < 0 || resultPerPage <= 0) {
            response.put("error", "Invalid search parameters");
            return response;
        }

        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(
                ResourceResolverFactory.SUBSERVICE,
                "data-reader"
        );

        try (ResourceResolver resourceResolver =
                     resourceResolverFactory.getServiceResourceResolver(authInfo)) {

            Session session = resourceResolver.adaptTo(Session.class);

            if (session == null) {
                response.put("error", "Unable to obtain JCR session");
                return response;
            }

            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(
                            createTextSearchQuery(
                                    searchText,
                                    startResult,
                                    resultPerPage
                            )
                    ),
                    session
            );

            SearchResult result = query.getResult();

            long totalResults = result.getTotalMatches();
            long startingResult = result.getStartIndex();

            int totalPages = (int) Math.ceil(
                    (double) totalResults / resultPerPage
            );

            JSONArray resultsArray = new JSONArray();

            for (Hit hit : result.getHits()) {
                Page page = hit.getResource().adaptTo(Page.class);

                if (page != null) {
                    JSONObject pageObject = new JSONObject();

                    pageObject.put("title", page.getTitle());
                    pageObject.put("path", page.getPath());

                    resultsArray.put(pageObject);
                }
            }

            response.put("searchText", searchText);
            response.put("perPageResult", resultsArray.length());
            response.put("totalResults", totalResults);
            response.put("startingResult", startingResult);
            response.put("resultPerPage", resultPerPage);
            response.put("currentPage",
                    (startResult / resultPerPage) + 1);
            response.put("totalPages", totalPages);
            response.put("results", resultsArray);

        } catch (Exception e) {
            LOG.error("Error executing QueryBuilder search", e);
            response.put("error", "Search failed");
        }

        return response;
    }
}
