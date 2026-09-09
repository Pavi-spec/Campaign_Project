alert("author.drive JS loaded");

(function (window, $) {

    "use strict";

    $(window).adaptTo("foundation-registry").register(
        "foundation.validation.validator",
        {
            selector: ".cq-RichText-editable",

            validate: function (element) {

                var maxLength = $(element)
                    .closest("[data-maxlength]")
                    .data("maxlength");

                if (!maxLength) {
                    return;
                }

                var text = $(element).text().trim();

                if (text.length > maxLength) {
                    return "only " + maxLength + " characters are allowed.";
                }
            }
        }
    );

})(window, Granite.$);