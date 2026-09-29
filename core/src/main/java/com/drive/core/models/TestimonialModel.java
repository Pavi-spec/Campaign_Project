package com.drive.core.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TestimonialModel {

    @Inject
    private String title;

    @ChildResource(name = "testimonials")
    private List<Resource> testimonials;

    public String getTitle() {
        return title;
    }

    public List<TestimonialItem> getTestimonials() {

        List<TestimonialItem> items = new ArrayList<>();

        if (testimonials != null) {

            for (Resource resource : testimonials) {

                items.add(
                        new TestimonialItem(resource)
                );
            }
        }

        return items;
    }

    public static class TestimonialItem {

        private final String name;
        private final String rating;
        private final String review;
        private final String source;
        private final String date;

        public TestimonialItem(Resource resource) {

            this.name =
                    resource.getValueMap().get(
                            "name",
                            ""
                    );

            this.rating =
                    resource.getValueMap().get(
                            "rating",
                            "0"
                    );

            this.review =
                    resource.getValueMap().get(
                            "review",
                            ""
                    );

            this.source =
                    resource.getValueMap().get(
                            "source",
                            ""
                    );

            this.date =
                    formatDate(
                            resource.getValueMap().get(
                                    "date",
                                    ""
                            )
                    );
        }

        public String getName() {
            return name;
        }

        public String getRating() {
            return rating;
        }

        public String getReview() {
            return review;
        }

        public String getSource() {
            return source;
        }

        public String getDate() {
            return date;
        }

        public String getFilledStars() {

            int ratingValue =
                    getRatingValue();

            StringBuilder result =
                    new StringBuilder();

            for (
                    int i = 0;
                    i < ratingValue;
                    i++
            ) {

                result.append("★");
            }

            return result.toString();
        }

        public String getEmptyStars() {

            int ratingValue =
                    getRatingValue();

            StringBuilder result =
                    new StringBuilder();

            for (
                    int i = ratingValue;
                    i < 5;
                    i++
            ) {

                result.append("☆");
            }

            return result.toString();
        }

        private int getRatingValue() {

            try {

                int value =
                        Integer.parseInt(rating);

                if (value < 0) {
                    return 0;
                }

                if (value > 5) {
                    return 5;
                }

                return value;

            } catch (NumberFormatException e) {

                return 0;
            }
        }

        private String formatDate(String date) {

            if (
                    date == null ||
                            date.isEmpty()
            ) {

                return "";
            }

            try {

                ZonedDateTime dateTime =
                        ZonedDateTime.parse(date);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        );

                return dateTime.format(
                        formatter
                );

            } catch (Exception e) {

                return date;
            }
        }
    }
}