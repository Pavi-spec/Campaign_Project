(function ($, Granite) {
    "use strict";

    var ID_VALIDATION = "drive.component-id";
    var DESCRIPTION_VALIDATION = "drive.hero-description";

    var ID_PATTERN = /^[A-Za-z][A-Za-z0-9_-]*$/;
    var DESCRIPTION_MAX_LENGTH = 50;


    /* =========================================================
       ID VALIDATION
       ========================================================= */

    function getCurrentResourcePath(field) {

        var form = $(field).closest("form");

        if (!form.length) {
            return null;
        }

        return form.attr("action");
    }


    function validateUniqueId(field) {

        var value = $.trim($(field).val());

        /*
         * Empty value is handled by required="true"
         */
        if (!value) {
            return;
        }


        /*
         * Validate ID format
         *
         * Valid:
         * main-banner
         * hero1
         * hero_section
         *
         * Invalid:
         * 123hero
         * hero space
         * hero@banner
         */
        if (!ID_PATTERN.test(value)) {

            field.setCustomValidity(
                "ID must start with a letter and contain only letters, numbers, hyphens or underscores."
            );

            return;
        }


        var currentResourcePath = getCurrentResourcePath(field);

        if (!currentResourcePath) {

            field.setCustomValidity(
                "Unable to determine the current component."
            );

            return;
        }


        /*
         * Check whether another component on the
         * same page already uses this ID.
         */
        $.ajax({

            url: "/bin/aem-training/check-component-id",

            type: "GET",

            dataType: "json",

            async: false,

            data: {
                id: value,
                resource: currentResourcePath
            },

            success: function (response) {

                if (response && response.unique === true) {

                    field.setCustomValidity("");

                } else {

                    field.setCustomValidity(
                        "This ID is already used by another component on this page."
                    );
                }
            },

            error: function () {

                /*
                 * Do not allow saving if the server
                 * validation cannot be completed.
                 */
                field.setCustomValidity(
                    "Unable to validate the ID. Please try again."
                );
            }
        });
    }


    /* =========================================================
       RTE DESCRIPTION VALIDATION
       ========================================================= */

    function getDescriptionText(field) {

        /*
         * The RTE editable area is normally next to the
         * hidden/input element used to store the value.
         */
        var fieldContainer = $(field).closest(".coral-Form-fieldwrapper");

        var editable = fieldContainer.find(
            ".coral-RichText-editable"
        );

        if (editable.length) {

            /*
             * text() gets only visible text.
             *
             * So HTML formatting such as:
             *
             * <strong>Hello</strong>
             *
             * is counted as:
             *
             * Hello
             */
            return $.trim(editable.text());
        }


        /*
         * Fallback if the editable element cannot be found.
         */
        return $.trim($(field).val() || "");
    }


    function validateDescription(field) {

        var text = getDescriptionText(field);

        var characterCount = text.length;


        /*
         * Empty value is not handled here.
         * If description is required, use required="true"
         * in the dialog.
         */
        if (characterCount === 0) {

            field.setCustomValidity("");

            return;
        }


        /*
         * Maximum 50 visible characters.
         */
        if (characterCount > DESCRIPTION_MAX_LENGTH) {

            field.setCustomValidity(
                "Description must not exceed 50 characters. " +
                "Current count: " +
                characterCount +
                "."
            );

            return;
        }


        /*
         * Description is valid.
         */
        field.setCustomValidity("");
    }


    /* =========================================================
       REGISTER ID VALIDATION
       ========================================================= */

    $(window).adaptTo("foundation-registry").register(
        "foundation.validation.validator",
        {

            selector:
                "[data-validation='" +
                ID_VALIDATION +
                "']",

            validate: function (element) {

                validateUniqueId(element);

                return element.validationMessage || null;
            }
        }
    );


    /* =========================================================
       REGISTER DESCRIPTION RTE VALIDATION
       ========================================================= */

    $(window).adaptTo("foundation-registry").register(
        "foundation.validation.validator",
        {

            selector:
                "[data-validation='" +
                DESCRIPTION_VALIDATION +
                "']",

            validate: function (element) {

                validateDescription(element);

                return element.validationMessage || null;
            }
        }
    );


    /* =========================================================
       ID - VALIDATE WHEN USER TYPES
       ========================================================= */

    $(document).on(
        "input change",
        "[data-validation='" + ID_VALIDATION + "']",
        function () {

            this.setCustomValidity("");

            validateUniqueId(this);
        }
    );


    /* =========================================================
       RTE - VALIDATE WHEN USER TYPES
       ========================================================= */

    $(document).on(
        "input",
        "[data-validation='" + DESCRIPTION_VALIDATION + "']",
        function () {

            validateDescription(this);
        }
    );


    /*
     * RTE content is actually edited inside
     * .coral-RichText-editable.
     *
     * Therefore listen to input events from the
     * editable area also.
     */
    $(document).on(
        "input keyup paste",
        ".coral-RichText-editable",
        function () {

            var editable = $(this);

            var fieldContainer =
                editable.closest(".coral-Form-fieldwrapper");

            var field =
                fieldContainer.find(
                    "[data-validation='" +
                    DESCRIPTION_VALIDATION +
                    "']"
                );

            if (field.length) {

                validateDescription(field[0]);
            }
        }
    );


})(Granite.$, Granite);