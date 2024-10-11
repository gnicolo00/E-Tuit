<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/error404.css" type="text/css">
    <title>Ti sei perso? | E-Tuit.it</title>
</head>
<body>
    <%@ include file="../header/header.jsp"%>

    <div id="error404-container">
        <h1>Ops!<br>Ti sei perso?</h1>
        <p id="error-type">(Errore 404)</p>
        <p id="error-message">La pagina cercata è introvabile...</p>
        <form action="home-servlet" method="get">
            <input type="submit" class="standard-button" value="Torna alla Home">
        </form>
    </div>
</body>
</html>