document.addEventListener("DOMContentLoaded", function () {

    // Campaign Offer Button

    const campaignButton =
        document.querySelector(".campaign-btn");

    if (campaignButton) {

        campaignButton.addEventListener("click", function () {

            const msg =
                document.getElementById("offerMessage");

            if (msg) {

                msg.innerHTML =
                    "🎉 Offer Activated!";

            }

        });

    }

    // Campaign Card Buttons

    document.querySelectorAll(".details-btn")
        .forEach(button => {

            button.addEventListener("click", function () {

                const category =
                    this.dataset.category;

                const container =
                    document.getElementById("products");

                if (!container) {

                    console.error(
                        "Products container not found"
                    );

                    return;

                }

                container.innerHTML =
                    "<h3>Loading Products...</h3>";

                fetch(
                    "/bin/campaigns?category=" +
                    category
                )

                    .then(response => {

                        if (!response.ok) {

                            throw new Error(
                                "Failed to fetch products"
                            );

                        }

                        return response.json();

                    })

                    .then(data => {

                        container.innerHTML = "";

                        if (
                            !data.products ||
                            data.products.length === 0
                        ) {

                            container.innerHTML =
                                "<h3>No Products Available</h3>";

                            return;

                        }

                        data.products.forEach(product => {

                            container.innerHTML += `

                                <div class="product-card">

                                    <img
                                        src="${product.thumbnail}"
                                        alt="${product.title}"
                                        class="product-image">

                                    <h3>${product.title}</h3>

                                    <p class="price">
                                        ₹ ${product.price}
                                    </p>

                                    <button
                                        class="offer-btn">

                                        Select Product

                                    </button>

                                </div>

                            `;

                        });

                    })

                    .catch(error => {

                        console.error(error);

                        container.innerHTML =

                            "<h3>Unable To Load Products</h3>";

                    });

            });

        });

});


// Campaign List Click

document.addEventListener("click", function (event) {

    if (event.target.closest(".campaign-item")) {

        alert("Selected Campaign Offer Activated");

    }

});


// Product Button Click

document.addEventListener("click", function (event) {

    if (
        event.target.classList.contains(
            "offer-btn"
        )
    ) {

        alert(
            "Campaign Product  Added to your Card!"
        );

    }

});