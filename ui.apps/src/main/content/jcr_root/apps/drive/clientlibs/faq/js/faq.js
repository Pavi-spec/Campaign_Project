function toggleAccordion(button) {

    const content = button.nextElementSibling;
    const icon = button.querySelector(".course-details__icon");

    if (content.style.display === "none" ||
        content.style.display === "") {

        content.style.display = "block";
        icon.textContent = "−";

    } else {

        content.style.display = "none";
        icon.textContent = "+";

    }
}