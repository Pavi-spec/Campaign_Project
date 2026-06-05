document.addEventListener("DOMContentLoaded", function () {

    const button = document.querySelector(".campaign-btn");

    if(button){

        button.addEventListener("click", function(){

            const msg = document.getElementById("offerMessage");

            if(msg){
                msg.innerHTML = "🎉 Offer Activated!";
            }

        });

    }

});



document.addEventListener("click", function(event){

    if(event.target.classList.contains("details-btn")){

        alert("Campaign Details Loaded Successfully!");

    }

});

document.addEventListener("click", function(event){

    if(event.target.closest(".campaign-item")){

        alert("Campaign Selected");

    }

});

document.addEventListener("DOMContentLoaded", function () {

    const container =
        document.getElementById("productContainer");

    if (!container) {
        return;
    }

    const buttonText =
        document.getElementById("buttonText")?.value ||
        "View Offer";

    fetch("/bin/campaigns")

        .then(function (response) {

            if (!response.ok) {

                throw new Error(
                    "Failed to fetch campaign data"
                );

            }

            return response.json();

        })

        .then(function (data) {

            container.innerHTML = "";

            if (!data.products ||
                data.products.length === 0) {

                container.innerHTML =
                    "<h3>No Campaigns Available</h3>";

                return;
            }

            data.products.forEach(function (product) {

                const card =
                    document.createElement("div");

                card.className =
                    "product-card";

                card.innerHTML =

                    '<img src="' +
                    product.thumbnail +
                    '" alt="' +
                    product.title +
                    '" class="product-image">' +

                    '<h3>' +
                    product.title +
                    '</h3>' +

                    '<p class="price">₹ ' +
                    product.price +
                    '</p>' +

                    '<button class="offer-btn">' +
                    buttonText +
                    '</button>';

                container.appendChild(card);

            });

        })

        .catch(function (error) {

            console.error(error);

            container.innerHTML =
                "<h3>Unable To Load Campaign Data</h3>";

        });

});


document.addEventListener("click", function (event) {

    if (event.target.classList.contains("offer-btn")) {

        alert("Campaign Offer Activated!");

    }

});
