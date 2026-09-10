console.log("EMPLOYEE CHAPTER DIALOG JS LOADED");
(function (document, $) {
    "use strict";

    function toggleChapterDetails() {
        var $checkbox = $(".employee-chapter-toggle input[type='checkbox']");
        var $chapterDetails = $(".employee-chapter-details");

        if (!$checkbox.length || !$chapterDetails.length) {
            return;
        }

        if ($checkbox.is(":checked")) {
            $chapterDetails.show();
        } else {
            $chapterDetails.hide();
        }
    }

    $(document).on("foundation-contentloaded", function () {
        toggleChapterDetails();
    });

    $(document).on(
        "change",
        ".employee-chapter-toggle input[type='checkbox']",
        function () {
            toggleChapterDetails();
        }
    );

})(document, Granite.$);