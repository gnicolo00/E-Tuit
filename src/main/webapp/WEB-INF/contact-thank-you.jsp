<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/contact-thank-you.css" type="text/css">
    <title>Grazie per averci contattato! | E-Tuit.it</title>
</head>
<body>
    <%@ include file="header/header.jsp"%>

    <div id="thank-you">
        <h1>Grazie per averci contattato!</h1>
        <p>Ti risponderemo al più presto. Nel frattempo, va a conoscere <span>i nostri tutor</span>!</p>

        <form action="home-servlet" method="get">
            <button type="submit" class="standard-button">Torna alla Home</button>
        </form>
        <form action="show-tutors" method="get">
            <button type="submit" class="standard-button">Visualizza i Tutor</button>
        </form>
    </div>

</body>
</html>
