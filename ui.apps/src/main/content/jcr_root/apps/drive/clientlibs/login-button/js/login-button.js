
(function () {
    "use strict";

    function initializeLoginButtons() {
        document.querySelectorAll("[data-login-submit]").forEach(function (button) {

            if (button.dataset.standaloneHandlerAttached === "true") {
                return;
            }

            button.dataset.standaloneHandlerAttached = "true";

            button.addEventListener("click", function (event) {

                // Buttons inside the Login form are handled by the Login JS.
                const form = button.closest("[data-login-form]");

                if (form) {
                    return;
                }

                event.preventDefault();

                const destination =
                    button.getAttribute("data-login-destination");

                const openInNewTab =
                    button.getAttribute("data-login-new-tab") === "true";

                console.log("Button destination:", destination);
                console.log("Open in new tab:", openInNewTab);

                if (!destination || !destination.trim()) {
                    console.error("Configure Button Link in the dialog.");
                    return;
                }

                if (openInNewTab) {
                    // Open directly from the user's click.
                    window.open(destination, "_blank");
                } else {
                    window.location.assign(destination);
                }
            });
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeLoginButtons);
    } else {
        initializeLoginButtons();
    }
})();
