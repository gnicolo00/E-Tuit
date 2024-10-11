<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Tutor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Tutor> experiencedTutors = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");
%>

<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/home.css" type="text/css">
    <link rel="stylesheet" href="css/card.css" type="text/css">
    <link rel="stylesheet" href="css/tutors-grid.css" type="text/css">
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/actions.js" type="text/javascript"></script>
    <script src="js/display.js" type="text/javascript"></script>
    <title>E-Tuit: Teachers On-Demand</title>
</head>
<body>
<%@ include file="WEB-INF/header/header.jsp"%>

<div id="search-container">
    <div id="index-bg">
    </div>
    <form id="search" action="show-tutors" method="get">
        <input type="text" list="suggestions" id="search-bar" name="search-bar" onkeyup="showSuggestions('suggestions', this.value)" placeholder="Cerca soggetti come &quot;Informatica&quot;, &quot;Biologia&quot;, ecc..">
        <datalist id="suggestions"></datalist>
        <button type="submit" id="search-button" class="standard-button"><img src="img/search.png" alt="search image"></button>
    </form>
</div>

<h1 id="title">Scopri i nostri <span>E-Tutors</span>!</h1>

<div class="card-container" id="card-container-home">
    <%for (Tutor t : experiencedTutors) {%>
        <div class="card">
            <%
                String imagePath = "propics_by_email/" + t.getEmailUtente().replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
                File imageFile = new File(request.getServletContext().getRealPath(imagePath));
                boolean profileImageExists = imageFile.exists();
            %>
            <%if (!profileImageExists) {%>
            <img src="img/default-user-icon.jpg" alt="Immagine del profilo." class="card-image">
            <%} else {%>
            <img src="<%=imagePath%>" alt="Immagine del profilo." class="card-image">
            <%}%>

            <div class="card-button">
                <form action="tutor-page" method="get" onsubmit="return validateTutorEmailCard()">
                    <input type="hidden" id="tutorEmail" name="tutorEmail" value="<%=t.getEmailUtente()%>">
                    <button type="submit" name="arrow-button" class="arrow-icon"><img src="img/arrow.png" alt="Bottone per la pagina del tutor."></button>
                </form>
            </div>

            <div class="card-content">
                <h3><%=t.getNome()%> <%=t.getCognome()%></h3>
                <div class="average-rating"><img src="img/rating-star.png" alt="Stella di recensione."><span> <%=t.getRecensioneMedia()%></span></div>
                <div class="subjects-tags">
                    <%for (String subject : t.getArraySettori()) {%>
                    <div class="subject"><%=subject.substring(0,1).toUpperCase() + subject.substring(1)%></div>
                    <%}%>
                </div>
                <div class="card-description">
                    <p><%=t.getDescrizioneCard()%></p>
                </div>
            </div>

            <div class="hourly-rate">
                <%=t.getTariffaOraria()%> €<span>/h</span>
            </div>
        </div>
    <%}%>
</div>

<form action="show-tutors" method="get">
    <input type="submit" id="show-more-home" value="Visualizza altri Tutor" class="standard-button">
</form>
</body>
</html>
