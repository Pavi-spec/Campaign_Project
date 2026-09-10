(function (document, $) {
    "use strict";

    function showHide(element) {

        var target = element.data("cqDialogCheckboxShowhideTarget");

        if (!target) {
            return;
        }

        var checked = element.prop("checked");

        var value = checked ? element.val() : "";

        $(target).not(".hide").addClass("hide");

        $(target)
            .filter("[data-showhidetargetvalue='" + value + "']")
            .removeClass("hide");
    }

    $(document).on("foundation-contentloaded", function () {

        $(".cq-dialog-checkbox-showhide").each(function () {
            showHide($(this));
        });

    });

    $(document).on(
        "change",
        ".cq-dialog-checkbox-showhide",
        function () {
            showHide($(this));
        }
    );

})(document, Granite.$);

console.log("Welcome to Chennai");