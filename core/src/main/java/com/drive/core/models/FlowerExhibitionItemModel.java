
package com.drive.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FlowerExhibitionItemModel {

    @ValueMapValue
    private String category;

    @ValueMapValue
    private String flowerDate;

    @ValueMapValue
    private String readingTime;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String description;


    @ValueMapValue
    private String articleLink;


    @ValueMapValue
    private String descriptionLink;

    @ValueMapValue
    private String linkText;

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

    public String getCategory() {
        return category;
    }

    public String getFlowerDate() {
        if (flowerDate == null || flowerDate.trim().isEmpty()) {
            return "";
        }

        String dateValue = flowerDate.trim();

        try {
            return OffsetDateTime.parse(dateValue)
                    .toLocalDate()
                    .format(OUTPUT_DATE_FORMAT);
        } catch (DateTimeParseException ignored) {

        }

        try {
            return LocalDateTime.parse(dateValue)
                    .toLocalDate()
                    .format(OUTPUT_DATE_FORMAT);
        } catch (DateTimeParseException ignored) {

        }

        try {
            return LocalDate.parse(dateValue)
                    .format(OUTPUT_DATE_FORMAT);
        } catch (DateTimeParseException ignored) {
            return dateValue;
        }
    }

    public String getReadingTime() {
        return readingTime;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        if (title != null && !title.trim().isEmpty()) {
            return title.trim();
        }

        return "Flower image";
    }

    public String getDescription() {
        return description;
    }


    public String getArticleLink() {
        return normalizeLink(articleLink);
    }

    public String getDescriptionLink() {
        return normalizeLink(descriptionLink);
    }

    public String getLinkText() {
        return linkText == null ? "" : linkText.trim();
    }

    public boolean isButtonDestinationAvailable() {
        return articleLink != null && !articleLink.trim().isEmpty();
    }

    public boolean isDescriptionDestinationAvailable() {
        return descriptionLink != null && !descriptionLink.trim().isEmpty();
    }

    public boolean isImageAvailable() {
        return image != null && !image.trim().isEmpty();
    }

    private String normalizeLink(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        String link = value.trim();


        if (link.startsWith("http://")
                || link.startsWith("https://")
                || link.startsWith("mailto:")
                || link.startsWith("tel:")
                || link.startsWith("#")) {
            return link;
        }

        int queryIndex = link.indexOf('?');
        int fragmentIndex = link.indexOf('#');
        int suffixIndex;

        if (queryIndex >= 0 && fragmentIndex >= 0) {
            suffixIndex = Math.min(queryIndex, fragmentIndex);
        } else if (queryIndex >= 0) {
            suffixIndex = queryIndex;
        } else {
            suffixIndex = fragmentIndex;
        }

        String path = suffixIndex >= 0
                ? link.substring(0, suffixIndex)
                : link;

        String suffix = suffixIndex >= 0
                ? link.substring(suffixIndex)
                : "";


        while (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.startsWith("/content/")
                && !path.startsWith("/content/dam/")
                && !path.endsWith(".html")) {
            path += ".html";
        }

        return path + suffix;
    }
}
