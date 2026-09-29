document.addEventListener("DOMContentLoaded", function () {

    const carousels =
        document.querySelectorAll(".testimonial-carousel");

    carousels.forEach(function (carousel) {

        const track =
            carousel.querySelector(
                ".testimonial-carousel__track"
            );

        const cards =
            carousel.querySelectorAll(
                ".testimonial-card"
            );

        const previous =
            carousel.querySelector(
                ".testimonial-carousel__previous"
            );

        const next =
            carousel.querySelector(
                ".testimonial-carousel__next"
            );
         const dots =
             carousel.querySelectorAll(
                 ".testimonial-carousel__dot"
             );

        const modal =
            carousel.querySelector(
                "[data-testimonial-modal]"
            );

        const closeButton =
            carousel.querySelector(
                "[data-testimonial-close]"
            );

        const modalName =
            carousel.querySelector(
                "[data-modal-name]"
            );

        const modalFilledStars =
            carousel.querySelector(
                "[data-modal-filled-stars]"
            );

        const modalEmptyStars =
            carousel.querySelector(
                "[data-modal-empty-stars]"
            );

        const modalReview =
            carousel.querySelector(
                "[data-modal-review]"
            );

        const modalSource =
            carousel.querySelector(
                "[data-modal-source]"
            );

        const modalDate =
            carousel.querySelector(
                "[data-modal-date]"
            );

        if (track && cards.length > 0) {

            let currentIndex = 0;


            function getCardsVisible() {

                if (window.innerWidth <= 600) {
                    return 1;
                }

                if (window.innerWidth <= 900) {
                    return 2;
                }

                return 3;
            }
            function updateDots() {

                const cardsVisible =
                    getCardsVisible();

                dots.forEach(function (dot, index) {

                    if (
                        index >= currentIndex &&
                        index < currentIndex + cardsVisible
                    ) {

                        dot.classList.add("is-active");

                    } else {

                        dot.classList.remove("is-active");
                    }

                });
            }

            function moveCarousel() {

                const cardWidth =
                    cards[0].offsetWidth;

                const cardsVisible =
                    getCardsVisible();

                const maximumIndex =
                    Math.max(
                        cards.length - cardsVisible,
                        0
                    );

                currentIndex =
                    Math.min(
                        currentIndex,
                        maximumIndex
                    );

                track.style.transform =
                    "translateX(-" +
                    (currentIndex * cardWidth) +
                    "px)";
                  updateDots();
            }

             moveCarousel();
            if (next) {

                next.addEventListener(
                    "click",
                    function () {

                        const cardsVisible =
                            getCardsVisible();

                        const maximumIndex =
                            Math.max(
                                cards.length - cardsVisible,
                                0
                            );

                        if (
                            currentIndex <
                            maximumIndex
                        ) {

                            currentIndex++;

                        } else {

                            currentIndex = 0;
                        }

                        moveCarousel();
                    }
                );
            }

            if (previous) {

                previous.addEventListener(
                    "click",
                    function () {

                        const cardsVisible =
                            getCardsVisible();

                        const maximumIndex =
                            Math.max(
                                cards.length - cardsVisible,
                                0
                            );

                        if (currentIndex > 0) {

                            currentIndex--;

                        } else {

                            currentIndex =
                                maximumIndex;
                        }

                        moveCarousel();
                    }
                );
            }

            window.addEventListener(
                "resize",
                moveCarousel
            );

            setInterval(function () {

                const cardsVisible =
                    getCardsVisible();

                const maximumIndex =
                    Math.max(
                        cards.length - cardsVisible,
                        0
                    );

                if (
                    currentIndex <
                    maximumIndex
                ) {

                    currentIndex++;

                } else {

                    currentIndex = 0;
                }

                moveCarousel();

            }, 5000);
        }

        if (!modal) {
            return;
        }

        function openModal(card) {

            const name =
                card.querySelector(
                    ".testimonial-card__name"
                );

            const filledStars =
                card.querySelector(
                    ".testimonial-card__rating--filled"
                );

            const emptyStars =
                card.querySelector(
                    ".testimonial-card__rating--empty"
                );

            const review =
                card.querySelector(
                    ".testimonial-card__review"
                );

            const source =
                card.querySelector(
                    ".testimonial-card__footer strong"
                );

            const date =
                card.querySelector(
                    ".testimonial-card__footer span"
                );

            if (
                modalName &&
                name
            ) {

                modalName.textContent =
                    name.textContent.trim();
            }

            if (
                modalFilledStars &&
                filledStars
            ) {

                modalFilledStars.textContent =
                    filledStars.textContent.trim();
            }

            if (
                modalEmptyStars &&
                emptyStars
            ) {

                modalEmptyStars.textContent =
                    emptyStars.textContent.trim();
            }

            if (
                modalReview &&
                review
            ) {

                modalReview.textContent =
                    review.textContent.trim();
            }

            if (
                modalSource &&
                source
            ) {

                modalSource.textContent =
                    source.textContent.trim();
            }

            if (
                modalDate &&
                date
            ) {

                modalDate.textContent =
                    date.textContent.trim();
            }

            modal.classList.add(
                "is-open"
            );

            modal.setAttribute(
                "aria-hidden",
                "false"
            );

            document.body.style.overflow =
                "hidden";
        }

        function closeModal() {

            modal.classList.remove(
                "is-open"
            );

            modal.setAttribute(
                "aria-hidden",
                "true"
            );

            document.body.style.overflow =
                "";
        }

        cards.forEach(function (card) {

            card.addEventListener(
                "click",
                function () {

                    openModal(card);
                }
            );

            card.addEventListener(
                "keydown",
                function (event) {

                    if (
                        event.key === "Enter" ||
                        event.key === " "
                    ) {

                        event.preventDefault();

                        openModal(card);
                    }
                }
            );

        });

        if (closeButton) {

            closeButton.addEventListener(
                "click",
                closeModal
            );
        }

        modal.addEventListener(
            "click",
            function (event) {

                if (
                    event.target === modal
                ) {

                    closeModal();
                }
            }
        );

        document.addEventListener(
            "keydown",
            function (event) {

                if (
                    event.key === "Escape" &&
                    modal.classList.contains(
                        "is-open"
                    )
                ) {

                    closeModal();
                }
            }
        );

    });

});