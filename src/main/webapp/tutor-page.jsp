<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Tutor" %>

<!--
JSP dedicata alla visualizzazione del singolo tutor.
-->

<!DOCTYPE html>
<html>
<%
    Tutor tutor = (Tutor) request.getAttribute("tutor-tutorPage");
    String imagePath = "propics_by_email/" + tutor.getEmailUtente().replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
    File imageFile = new File(request.getServletContext().getRealPath(imagePath));
    boolean profileImageExists = imageFile.exists();
%>

<head>
    <title><%=tutor.getNome()%> <%=tutor.getCognome()%> | E-Tuit.it</title>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/profile.css" type="text/css">
    <link rel="stylesheet" href="css/card.css" type="text/css">
    <link rel="stylesheet" href="css/tutor-page.css" type="text/css">
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/validation.js" type="text/javascript"></script>
    <script src="js/display.js" type="text/javascript"></script>
</head>

<body>
    <%@ include file="WEB-INF/header/header.jsp"%><br/>

    <div class="tutorpage-container">
        <div class="card">
            <%if (!profileImageExists) {%>
                <img src="img/default-user-icon.jpg" alt="Immagine del profilo." class="card-image">
            <%} else {%>
                <img src="<%=imagePath%>" alt="Immagine del profilo." class="card-image">
            <%}%>

            <div class="card-content">
                <h3><%=tutor.getNome()%> <%=tutor.getCognome()%></h3>

                <div class="average-rating"><img src="img/rating-star.png" alt="Stella di recensione."><span> <%=tutor.getRecensioneMedia()%></span></div>

                <p class="tutoring-hours"><%=tutor.getOreTutoring()%> ore di tutoring</p>

                <hr>

                <p class="hourly-rate-page">Tariffa oraria: <%=tutor.getTariffaOraria()%>€</p>

                <% if (request.getSession().getAttribute("tutor") == null) { %>
                    <button id="book-lesson-button" onclick="booking()" class="standard-button">Prenota una lezione</button>
                <%}%>
            </div>
        </div>

        <div id="book-lesson-container">
            <form id="book-lesson-form" action="book-lesson-servlet" method="get" onsubmit="return validateBookLesson()">
                <h3>Specifiche della lezione</h3>
                <input type="hidden" id="tutorEmail" name="tutorEmail" value="<%=tutor.getEmailUtente()%>">
                <label for="numOrePrenotate">Ore di lezione:</label>
                <input type="number" id="numOrePrenotate" name="numOrePrenotate" min="1" value="1" required>
                <p>Soggetto della lezione:</p>
                <%for(String settore : tutor.getArraySettori()) {%>
                    <input type="radio" id="<%=settore%>" name="materiaLezione" value="<%=settore%>" required>
                    <label for="<%=settore%>"><%=settore.substring(0,1).toUpperCase() + settore.substring(1)%></label><br/><br/>
                <%}%>
                <input type="submit" value="Aggiungi al carrello" class="standard-button">
                <button type="button" id="cancel-booking" class="standard-button" onclick="cancelBooking()">Annulla</button>
            </form>
        </div>

        <div class="tutor-info">
            <div class="scrollable-content">
                <!-- Contenuto scrollabile -->
                <h1><%=tutor.getDescrizioneCard()%></h1>

                <h2>Titoli</h2>
                <ul class="qualifications-list">
                    <%for (String titolo : tutor.getListaTitoli()) {%>
                        <li class="qualification"><%=titolo%></li>
                    <%}%>
                </ul>

                <h2>Riguardo <%=tutor.getNome()%></h2>
                <p><%=tutor.getDescrizioneCompleta()%></p>

                <h2>Scheduling</h2>
                <ul class="days-list">
                    <%for (String giorno : tutor.getListaGiorni()) {%>
                        <%if (giorno.equals("sabato")) {%>
                            <li>Tutti i sabati</li>
                        <%} else if (giorno.equals("domenica")) {%>
                            <li>Tutte le domeniche</li>
                        <%} else {%>
                            <li>Tutti i <%=giorno%></li>
                        <%}%>
                    <%}%>
                </ul>

                <%if(request.getSession().getAttribute("studente") != null){%>
                    <button id="add-rating-button" onclick="addRating()" class="standard-button">Aggiungi una recensione</button>
                    <form action="add-rating" id="add-rating-form" method="get" onsubmit="return validateEmail(<%=tutor.getEmailUtente()%>)">
                        <input type="hidden" name="tutorEmail" value="<%=tutor.getEmailUtente()%>">
                        <input type="submit" class="rating-button" name="rating" value="1" onclick="return validateRating(this.value)"><br/>
                        <input type="submit" class="rating-button" name="rating" value="2" onclick="return validateRating(this.value)"><br/>
                        <input type="submit" class="rating-button" name="rating" value="3" onclick="return validateRating(this.value)"><br/>
                        <input type="submit" class="rating-button" name="rating" value="4" onclick="return validateRating(this.value)"><br/>
                        <input type="submit" class="rating-button" name="rating" value="5" onclick="return validateRating(this.value)"><br/>
                    </form>
                <%}%>
            </div>
        </div>
    </div>
</body>
</html>