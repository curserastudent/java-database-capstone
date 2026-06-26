/**
 * ==========================================================
 * File: footer.js
 * Description:
 * Dynamically renders the application footer on every page.
 * ==========================================================
 */

/**
 * Renders the application footer.
 */
function renderFooter() {

    /**
     * Get the footer placeholder from the DOM.
     */
    const footer = document.getElementById("footer");

    if (!footer) {
        return;
    }

    /**
     * Insert footer content.
     */
    footer.innerHTML = `
        <footer class="footer">

            <div class="footer-container">

                <!-- Logo Section -->
                <div class="footer-logo">

                    <img
                        src="../assets/images/logo/Logo.png"
                        alt="Hospital CMS Logo">

                    <p>
                        &copy; Copyright 2025.
                        All Rights Reserved by Hospital CMS.
                    </p>

                </div>

                <!-- Links Section -->
                <div class="footer-links">

                    <!-- Company -->
                    <div class="footer-column">

                        <h4>Company</h4>

                        <a href="#">
                            About
                        </a>

                        <a href="#">
                            Careers
                        </a>

                        <a href="#">
                            Press
                        </a>

                    </div>

                    <!-- Support -->
                    <div class="footer-column">

                        <h4>Support</h4>

                        <a href="#">
                            Account
                        </a>

                        <a href="#">
                            Help Center
                        </a>

                        <a href="#">
                            Contact Us
                        </a>

                    </div>

                    <!-- Legal -->
                    <div class="footer-column">

                        <h4>Legals</h4>

                        <a href="#">
                            Terms &amp; Conditions
                        </a>

                        <a href="#">
                            Privacy Policy
                        </a>

                        <a href="#">
                            Licensing
                        </a>

                    </div>

                </div>

            </div>

        </footer>
    `;
}

/**
 * Expose the function globally.
 */
window.renderFooter = renderFooter;

/**
 * Render the footer when the page finishes loading.
 */
document.addEventListener("DOMContentLoaded", renderFooter);
