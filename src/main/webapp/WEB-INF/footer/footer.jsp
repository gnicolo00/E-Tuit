<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="../css/base.css" type="text/css">
    <title>Footer</title>
</head>
<body>
    <footer>
        <div id="footer-container">
            <div id="footer-about" class="footer-column">
                <h4>A proposito</h4>
                <ul>
                    <li><a href="about-us.jsp">Chi siamo</a></li>
                    <li><a href="privacy-policy.jsp">Info sulla privacy</a></li>
                </ul>
            </div>
            <div id="footer-support" class="footer-column">
                <h4>Supporto</h4>
                <ul>
                    <li><a href="come-funziona.jsp">Bisogno di aiuto?</a></li>
                    <li><a href="contact-us.jsp">Contatta il supporto</a></li>
                </ul>
            </div>
            <div id="footer-social" class="footer-column">
                <h4>Seguici</h4>
                <button class="social-button" id="facebook" onclick="location.href='https://www.facebook.com/'"></button>
                <button class="social-button" id="instagram" onclick="location.href='https://www.instagram.com/'"></button>
                <button class="social-button" id="twitter" onclick="location.href='https://twitter.com/'"></button>
            </div>
        </div>
        <div id="copyright">
            <hr>
            <p>&copy; 2023 E-Tuit.it | Connettiti. Impara.</p>
        </div>
    </footer>
</body>
</html>
