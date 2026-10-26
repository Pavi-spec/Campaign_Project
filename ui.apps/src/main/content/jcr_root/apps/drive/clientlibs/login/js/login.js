document.addEventListener("DOMContentLoaded", function () {
    "use strict";

    const forms = document.querySelectorAll("[data-login-form]");

    forms.forEach(function (form) {
        if (form.dataset.loginHandlerAttached === "true") {
            return;
        }

        form.dataset.loginHandlerAttached = "true";

        let loginTargetTab = null;
        let loginCsrfToken = "";

        const loginButton = form.querySelector("[data-login-submit]");

        function closeLoginTargetTab() {
            if (loginTargetTab && !loginTargetTab.closed) {
                loginTargetTab.close();
            }

            loginTargetTab = null;
        }

        // Open a temporary tab directly from the user's click.
        if (loginButton) {
            loginButton.addEventListener("click", function () {
                const openInNewTab =
                    loginButton.getAttribute("data-login-new-tab") === "true";

                if (!openInNewTab) {
                    closeLoginTargetTab();
                    return;
                }

                closeLoginTargetTab();
                loginTargetTab = window.open("about:blank", "_blank");

                if (!loginTargetTab) {
                    console.error(
                        "Pop-up blocked. Allow pop-ups and try again."
                    );
                }
            });
        }

        // Handle login form submission.
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const fields = form.querySelectorAll("[data-login-field]");

            const emailError = form.querySelector(
                '[data-field-error="email"]'
            );

            const passwordError = form.querySelector(
                '[data-field-error="password"]'
            );

            // Clear previous validation messages.
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

            // Read configured fields.
            fields.forEach(function (field) {
                const fieldType = (
                    field.getAttribute("data-field-type") || ""
                ).toLowerCase().trim();

                if (fieldType === "email") {
                    hasEmailField = true;
                    email = field.value.trim();
                }

                if (fieldType === "password") {
                    hasPasswordField = true;
                    password = field.value;
                }
            });

            // At least one field must be configured.
            if (!hasEmailField && !hasPasswordField) {
                console.error(
                    "Configure Email, Password, or both in the Login dialog."
                );
                closeLoginTargetTab();
                return;
            }

            let hasValidationError = false;

            // Validate email independently.
            if (hasEmailField) {
                if (!email) {
                    if (emailError) {
                        emailError.textContent = "Email is required.";
                    }

                    hasValidationError = true;
                } else if (
                    !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
                ) {
                    if (emailError) {
                        emailError.textContent =
                            "Enter a valid email address.";
                    }

                    hasValidationError = true;
                }
            }

            // Validate password independently.
            if (hasPasswordField) {
                if (!password.trim()) {
                    if (passwordError) {
                        passwordError.textContent =
                            "Password is required.";
                    }

                    hasValidationError = true;
                }
            }

            console.log("Email field configured:", hasEmailField);
            console.log("Password field configured:", hasPasswordField);
            console.log("Email validation message:",
                emailError ? emailError.textContent : "Error element missing");
            console.log("Password validation message:",
                passwordError ? passwordError.textContent : "Error element missing");

            // Stop before any network request if either field is invalid.
            if (hasValidationError) {
                closeLoginTargetTab();
                return;
            }

            // Prepare data for the login validation servlet.
            const formData = new URLSearchParams();

            if (hasEmailField) {
                formData.append("email", email);
            }

            if (hasPasswordField) {
                formData.append("password", password);
            }

            formData.append(
                "passwordRequired",
                hasPasswordField ? "true" : "false"
            );

            const submitButton = form.querySelector(
                'button[type="submit"]'
            );

            if (submitButton) {
                submitButton.disabled = true;
            }

            // Retrieve the AEM CSRF token.
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

                    loginCsrfToken = csrfData.token;

                    // Validate the configured fields in the servlet.
                    return fetch("/bin/drive/login", {
                        method: "POST",
                        headers: {
                            "Content-Type":
                                "application/x-www-form-urlencoded",
                            "CSRF-Token": loginCsrfToken
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
                    // Display server-side validation messages.
                    if (emailError) {
                        emailError.textContent =
                            data.emailMessage || "";
                    }

                    if (passwordError) {
                        passwordError.textContent =
                            data.passwordMessage || "";
                    }

                    // Do not continue when validation fails.
                    if (data.success !== true) {
                        closeLoginTargetTab();
                        return;
                    }

                    const destination = loginButton
                        ? loginButton.getAttribute(
                            "data-login-destination"
                        )
                        : "";

                    const openInNewTab = !!loginButton &&
                        loginButton.getAttribute(
                            "data-login-new-tab"
                        ) === "true";

                    if (!destination || !destination.trim()) {
                        console.error(
                            "Configure Button Link in the Login Button dialog."
                        );
                        closeLoginTargetTab();
                        return;
                    }

                    // Save metadata only. Never store the raw password.
                    if (loginCsrfToken) {
                        const storageData = new URLSearchParams();

                        if (hasEmailField) {
                            storageData.append("email", email);
                        }

                        storageData.append(
                            "passwordRequired",
                            hasPasswordField ? "true" : "false"
                        );

                        fetch("/bin/drive/login-data", {
                            method: "POST",
                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded",
                                "CSRF-Token": loginCsrfToken
                            },
                            body: storageData.toString(),
                            credentials: "same-origin",
                            keepalive: true
                        })
                            .then(function (response) {
                                if (!response.ok) {
                                    throw new Error(
                                        "Login data save failed with HTTP " +
                                        response.status
                                    );
                                }

                                return response.json();
                            })
                            .then(function (saveResult) {
                                if (saveResult.success === true) {
                                    console.log(
                                        "Login submission metadata saved successfully."
                                    );
                                } else {
                                    console.error(
                                        "Login submission was not saved.",
                                        saveResult
                                    );
                                }
                            })
                            .catch(function (error) {
                                console.error(
                                    "Login data storage error:",
                                    error
                                );
                            });
                    }

                    // Preserve the original navigation behavior.
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
                        closeLoginTargetTab();
                        window.location.assign(destination);
                    }
                })
                .catch(function (error) {
                    console.error("Login request failed:", error);
                    closeLoginTargetTab();
                })
                .finally(function () {
                    if (submitButton) {
                        submitButton.disabled = false;
                    }
                });
        });
    });

    // Handle login buttons used outside the login form.
    document.querySelectorAll("[data-login-submit]").forEach(
        function (button) {
            // Buttons inside a login form are handled by the form.
            if (button.closest("[data-login-form]")) {
                return;
            }

            if (button.dataset.standaloneHandlerAttached === "true") {
                return;
            }

            button.dataset.standaloneHandlerAttached = "true";

            button.addEventListener("click", function (event) {
                event.preventDefault();

                const destination = button.getAttribute(
                    "data-login-destination"
                );

                const openInNewTab =
                    button.getAttribute("data-login-new-tab") === "true";

                if (!destination || !destination.trim()) {
                    console.error(
                        "Configure Button Link in the Login Button dialog."
                    );
                    return;
                }

                if (openInNewTab) {
                    window.open(destination, "_blank");
                } else {
                    window.location.assign(destination);
                }
            });
        }
    );
});
