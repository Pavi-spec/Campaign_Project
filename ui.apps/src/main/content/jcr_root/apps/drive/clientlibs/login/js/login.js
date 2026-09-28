document.addEventListener("DOMContentLoaded", function () {

    const forms = document.querySelectorAll("[data-login-form]");

    forms.forEach(function (form) {

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

            fields.forEach(function (field) {

                const fieldType =
                    field.getAttribute("data-field-type");

                if (fieldType === "email") {
                    email = field.value.trim();
                }

                if (fieldType === "password") {
                    password = field.value;
                }

            });

            console.log("Email entered:", email);
            console.log("Password entered:", password);

            const formData = new URLSearchParams();

            formData.append("email", email);
            formData.append("password", password);

            fetch("/libs/granite/csrf/token.json")

                .then(function (response) {

                    console.log(
                        "CSRF token HTTP status:",
                        response.status
                    );

                    return response.json();

                })

                .then(function (csrfData) {

                    console.log("CSRF token received");

                    return fetch(
                        "/bin/drive/login",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/x-www-form-urlencoded",

                                "CSRF-Token":
                                    csrfData.token
                            },

                            body: formData.toString()
                        }
                    );

                })

                .then(function (response) {

                    console.log(
                        "Servlet HTTP status:",
                        response.status
                    );

                    return response.json();

                })

                .then(function (data) {

                    console.log(
                        "Servlet response:",
                        data
                    );

                    if (data.emailMessage) {

                        console.error(
                            "Email error:",
                            data.emailMessage
                        );

                        if (emailError) {
                            emailError.textContent =
                                data.emailMessage;
                        }
                    }

                    if (data.passwordMessage) {

                        console.error(
                            "Password error:",
                            data.passwordMessage
                        );

                        if (passwordError) {
                            passwordError.textContent =
                                data.passwordMessage;
                        }
                    }

                    if (data.success === true) {

                        console.log(
                            "Login successful!"
                        );

                    }

                })

                .catch(function (error) {

                    console.error(
                        "Servlet request failed:",
                        error
                    );

                });

        });

    });

});