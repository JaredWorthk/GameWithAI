function toggleForms() {
    var loginForm = document.getElementById("loginForm");
    var registerForm = document.getElementById("registerForm");

    loginForm.classList.toggle("hidden");
    registerForm.classList.toggle("hidden");

    document.querySelector('p[style*="color: red"]').innerText = "";
}

// $(document).ready(function () {
// })