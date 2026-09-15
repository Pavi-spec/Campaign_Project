function toggleSubjectAccordion(button) {

    const content = button.nextElementSibling;

    const icon = button.querySelector(".subject-info-icon");

    if (content.style.display === "block") {

        content.style.display = "none";

        icon.textContent = "+";

    } else {

        content.style.display = "block";

        icon.textContent = "−";
    }
}