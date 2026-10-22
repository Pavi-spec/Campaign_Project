(function () {
    "use strict";

    function isEditMode(component) {
        var authorMode = component.getAttribute("data-author-mode");

        return authorMode === "true" ||
            (window.Granite &&
             window.Granite.author &&
             typeof window.Granite.author === "object");
    }

    function initialize(component) {
        var deck = component.querySelector(".flower-details__deck");
        var navigation = component.querySelector(
            "[data-flower-navigation]"
        );

        if (!deck) {
            return;
        }

        var editMode = isEditMode(component);


        if (editMode) {
            if (navigation) {
                navigation.innerHTML = "";
                navigation.style.setProperty("display", "none", "important");
            }

            var editCards = deck.querySelectorAll(
                ".flower-details__slide"
            );

            editCards.forEach(function (card) {
                card.style.setProperty("display", "grid", "important");
                card.style.setProperty("visibility", "visible", "important");
                card.style.setProperty("opacity", "1", "important");
                card.setAttribute("aria-hidden", "false");

                var column = card.closest(".aem-GridColumn");

                if (column && deck.contains(column)) {
                    column.style.removeProperty("display");
                    column.style.removeProperty("visibility");
                    column.style.removeProperty("opacity");
                    column.style.setProperty("float", "none", "important");
                    column.style.setProperty("width", "100%", "important");
                }
            });

            return;
        }

        /*
         * PUBLISHED MODE
         */
        var cards = Array.from(
            deck.querySelectorAll(".flower-details__slide")
        );

        if (navigation) {
            navigation.innerHTML = "";
        }

        if (cards.length === 0) {
            return;
        }

        var items = cards.map(function (card) {
            var column = card.closest(".aem-GridColumn");

            if (!column || !deck.contains(column)) {
                column = card;
            }

            return {
                card: card,
                column: column
            };
        });

        var buttons = [];
        var activeIndex = 0;
        var duration = Number(component.getAttribute("data-interval")) || 10000;
        var animationFrame = null;
        var startTime = null;

        /*
         * One navigation circle per saved Flower Item.
         */
        if (navigation) {
            navigation.style.display = "flex";

            items.forEach(function (item, index) {
                var button = document.createElement("button");
                button.type = "button";
                button.className = "flower-details__dot";

                var initials = item.card.getAttribute("data-initials");

                button.textContent =
                    initials && initials.trim()
                        ? initials.trim()
                        : String(index + 1);

                button.setAttribute(
                    "aria-label",
                    "Show flower " + (index + 1)
                );
                button.setAttribute("aria-pressed", "false");

                button.addEventListener("click", function () {
                    showSlide(index);
                    startProgress();
                });

                navigation.appendChild(button);
                buttons.push(button);
            });
        }

        function showSlide(index) {
            activeIndex = (index + items.length) % items.length;

            items.forEach(function (item, i) {
                var active = i === activeIndex;

                item.column.style.display = active ? "" : "none";
                item.card.style.display = active ? "grid" : "none";
                item.card.setAttribute(
                    "aria-hidden",
                    active ? "false" : "true"
                );

                if (buttons[i]) {
                    buttons[i].classList.toggle("is-active", active);
                    buttons[i].setAttribute(
                        "aria-pressed",
                        active ? "true" : "false"
                    );
                    buttons[i].style.setProperty(
                        "--flower-progress",
                        "0%"
                    );
                }
            });
        }

        function animate(timestamp) {
            if (startTime === null) {
                startTime = timestamp;
            }

            var elapsed = timestamp - startTime;
            var progress = Math.min((elapsed / duration) * 100, 100);

            if (buttons[activeIndex]) {
                buttons[activeIndex].style.setProperty(
                    "--flower-progress",
                    progress + "%"
                );
            }

            if (progress >= 100) {
                showSlide(activeIndex + 1);
                startTime = null;
            }

            animationFrame = window.requestAnimationFrame(animate);
        }

        function startProgress() {
            if (animationFrame !== null) {
                window.cancelAnimationFrame(animationFrame);
            }

            startTime = null;

            if (buttons[activeIndex]) {
                buttons[activeIndex].style.setProperty(
                    "--flower-progress",
                    "0%"
                );
            }

            if (items.length > 1) {
                animationFrame = window.requestAnimationFrame(animate);
            }
        }

        showSlide(0);

        if (items.length > 1) {
            startProgress();
        }
    }

    function initializeAll() {
        document.querySelectorAll(".flower-details").forEach(initialize);
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeAll);
    } else {
        initializeAll();
    }
})();
