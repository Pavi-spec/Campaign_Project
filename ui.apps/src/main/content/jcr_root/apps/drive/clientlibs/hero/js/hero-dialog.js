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





    $(window).adaptTo("foundation-registry").register(
        "foundation.validation.validator",
        {
            selector:
                ".drive-hero-id input",

            validate: function (element) {

                var id =
                    (element.value || "").trim();




                if (!id) {

                    return "ID is required.";
                }


                var dialog =
                    $(element).closest(".cq-dialog");

                if (!dialog.length) {

                    return;
                }

                var form =
                    dialog.find("form").first();

                if (!form.length) {

                    return;
                }


                var componentPath =
                    form.attr("action");

                if (!componentPath) {

                    return "Unable to determine component path.";
                }


                componentPath =
                    componentPath.split("?")[0];


                var validationMessage = null;

                $.ajax({

                    url:
                        "/bin/drive/hero/validate-id",

                    type: "GET",

                    dataType: "json",

                    data: {

                        id: id,

                        componentPath:
                            componentPath
                    },

                    async: false,


                    success: function (data) {

                        console.log(
                            "Hero ID servlet response:",
                            data
                        );



                        if (data.valid === false) {

                            validationMessage =
                                data.message;

                            console.log(
                                "Validation message:",
                                validationMessage
                            );
                        }
                    },

                    error: function (xhr) {

                        console.log(
                            "Hero ID validation failed:",
                            xhr.status,
                            xhr.responseText
                        );

                        validationMessage =
                            "Unable to validate ID.";
                    }
                });

                return validationMessage;
            }
        }
    );


})(document, Granite.$);
