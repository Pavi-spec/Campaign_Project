
document.addEventListener("DOMContentLoaded", function () {

    const forms = document.querySelectorAll("[data-login-form]");

    forms.forEach(function (form) {

        // Avoid attaching the submit handler twice.
        if (form.dataset.loginHandlerAttached === "true") {
            return;
        }

        form.dataset.loginHandlerAttached = "true";

        let loginTargetTab = null;

        const loginButton = form.querySelector("[data-login-submit]");

        // Open a blank tab immediately from the user's click.
        // This helps prevent the browser from blocking the new tab
        // after the asynchronous validation request.
        if (loginButton) {
            loginButton.addEventListener("click", function () {

                const openInNewTab =
                    loginButton.getAttribute("data-login-new-tab") === "true";

                if (openInNewTab) {

                    if (loginTargetTab && !loginTargetTab.closed) {
                        loginTargetTab.close();
                    }

                    loginTargetTab = window.open("about:blank", "_blank");

                    if (!loginTargetTab) {
                        console.error(
                            "The browser blocked the new tab. Allow pop-ups and try again."
                        );
                    }
                }
            });
        }

        form.addEventListener("submit", function (event) {

            event.preventDefault();

            console.log("Login form submitted");

            const fields = form.querySelectorAll("[data-login-field]");

            const emailError = form.querySelector(
                '[data-field-error="email"]'
            );

            const passwordError = form.querySelector(
                '[data-field-error="password"]'
            );

            if (emailError) {
                emailError.textContent = "";
            }

            if (passwordError) {
                passwordError.textContent = "";
            }

            let email = "";
            let password = "";
            let hasEmailField = false;
            let hasPasswordField = false;

            // Read the fields configured in the AEM dialog.
            fields.forEach(function (field) {

                const fieldType = field.getAttribute("data-field-type");

                if (fieldType === "email") {
                    hasEmailField = true;
                    email = field.value.trim();
                }

                if (fieldType === "password") {
                    hasPasswordField = true;
                    password = field.value;
                }

            });

            if (!hasEmailField && !hasPasswordField) {
                console.error("No login fields are configured.");
                return;
            }

            const formData = new URLSearchParams();

            if (hasEmailField) {
                formData.append("email", email);
            }

            if (hasPasswordField) {
                formData.append("password", password);
            }

            // For configurable form validation only.
            formData.append(
                "passwordRequired",
                String(hasPasswordField)
            );

            const submitButton = form.querySelector(
                'button[type="submit"]'
            );

            if (submitButton) {
                submitButton.disabled = true;
            }

            fetch("/libs/granite/csrf/token.json")
                .then(function (response) {

                    if (!response.ok) {
                        throw new Error("Unable to retrieve CSRF token.");
                    }

                    return response.json();

                })
                .then(function (csrfData) {

                    if (!csrfData.token) {
                        throw new Error("CSRF token is missing.");
                    }

                    return fetch("/bin/drive/login", {
                        method: "POST",
                        headers: {
                            "Content-Type":
                                "application/x-www-form-urlencoded",
                            "CSRF-Token": csrfData.token
                        },
                        body: formData.toString()
                    });

                })
                .then(function (response) {

                    if (!response.ok) {
                        throw new Error(
                            "Servlet request failed with HTTP " +
                            response.status
                        );
                    }

                    return response.json();

                })
                .then(function (data) {

                    if (emailError && hasEmailField) {
                        emailError.textContent =
                            data.emailMessage || "";
                    }

                    if (passwordError && hasPasswordField) {
                        passwordError.textContent =
                            data.passwordMessage || "";
                    }

                    if (data.success === true) {

                        console.log("Form validation successful.");

                        const button = form.querySelector(
                            "[data-login-submit]"
                        );

                        if (!button) {
                            console.error("Login button not found.");
                            return;
                        }

                        const destination = button.getAttribute(
                            "data-login-destination"
                        );

                        const openInNewTab =
                            button.getAttribute("data-login-new-tab") === "true";

                        console.log("Destination:", destination);
                        console.log("Open in new tab:", openInNewTab);

                        if (!destination || !destination.trim()) {
                            console.error(
                                "Button Link is empty in the component dialog."
                            );

                            if (loginTargetTab && !loginTargetTab.closed) {
                                loginTargetTab.close();
                                loginTargetTab = null;
                            }

                            return;
                        }

                        if (openInNewTab) {

                            if (loginTargetTab && !loginTargetTab.closed) {
                                loginTargetTab.location.href = destination;
                                loginTargetTab = null;
                            } else {
                                console.error(
                                    "New tab unavailable. Allow pop-ups and try again."
                                );
                            }

                        } else {

                            if (loginTargetTab && !loginTargetTab.closed) {
                                loginTargetTab.close();
                                loginTargetTab = null;
                            }

                            window.location.assign(destination);
                        }

                    } else {

                        // Validation failed: do not navigate.
                        if (loginTargetTab && !loginTargetTab.closed) {
                            loginTargetTab.close();
                            loginTargetTab = null;
                        }

                    }

                })
                .catch(function (error) {

                    console.error("Login request failed:", error);

                    if (loginTargetTab && !loginTargetTab.closed) {
                        loginTargetTab.close();
                        loginTargetTab = null;
                    }

                })
                .finally(function () {

                    if (submitButton) {
                        submitButton.disabled = false;
                    }

                });

        });

    });

});
