
(function () {
    "use strict";

    function initializeFlowerExhibition() {
        var sections = document.querySelectorAll("[data-flower-exhibition]");

        sections.forEach(function (section) {
            if (section.dataset.flowerInitialized === "true") {
                return;
            }

            section.dataset.flowerInitialized = "true";

            section.addEventListener("click", function (event) {
                var link = event.target.closest(
                    ".flower-exhibition-item__image-button[data-flower-loading-link]"
                );

                if (!link || !section.contains(link)) {
                    return;
                }

                /* Preserve normal browser behaviour for modified clicks */
                if (
                    event.button !== 0 ||
                    event.ctrlKey ||
                    event.metaKey ||
                    event.shiftKey ||
                    event.altKey
                ) {
                    return;
                }

                if (link.target === "_blank" || link.hasAttribute("download")) {
                    return;
                }

                if (link.classList.contains("is-loading")) {
                    event.preventDefault();
                    return;
                }

                var destination = link.href;

                if (!destination) {
                    return;
                }

                event.preventDefault();

                var buttonText = link.querySelector(
                    ".flower-exhibition-item__button-text"
                );

                var arrow = link.querySelector(
                    ".flower-exhibition-item__button-arrow"
                );

                var loadingText = link.querySelector(
                    ".flower-exhibition-item__loading"
                );

                link.classList.add("is-loading");
                link.setAttribute("aria-busy", "true");

                if (buttonText) {
                    buttonText.hidden = true;
                }

                if (arrow) {
                    arrow.hidden = true;
                }

                if (loadingText) {
                    loadingText.hidden = false;
                }

                window.setTimeout(function () {
                    window.location.assign(destination);
                }, 350);
            });
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener(
            "DOMContentLoaded",
            initializeFlowerExhibition
        );
    } else {
        initializeFlowerExhibition();
    }
})();
