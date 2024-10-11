<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Tutor" %>
<%@ page import="com.mysql.cj.util.StringUtils" %>

<!-- Questa JSP consente di visualizzare tutti i tutor -->

<%
    ArrayList<Tutor> tutorList = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
    String givenInput = (String) request.getAttribute("given-input");
%>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/card.css" type="text/css">
    <link rel="stylesheet" href="css/tutors-grid.css" type="text/css">
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/actions.js" type="text/javascript"></script>
    <title>E-Tuit: Teachers On-Demand</title>
</head>
<body>
<%@ include file="WEB-INF/header/header.jsp"%><br/>

<!-- Barra di ricerca -->
<div id="search-container-grid">
    <input type="text" id="search-bar-grid" list="suggestions-grid" onkeyup="showSuggestions('suggestions-grid', this.value)" placeholder="Filtra...">
    <datalist id="suggestions-grid"></datalist>
    <button id="search-button-grid"></button>
</div>

<div id="tutor-grid-container">
    <!-- Pannello filtri -->
    <div id="pannello-filtri">
        <h2>Filtri</h2>
        <div id="filtro-giorni">
            <label><strong>Disponibilità:</strong></label>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="lun" name="giornoDisponibile" value="1" checked>
                <label for="lun">Lunedì</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="mar" name="giornoDisponibile" value="2" checked>
                <label for="mar">Martedì</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="mer" name="giornoDisponibile" value="3" checked>
                <label for="mer">Mercoledì</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="gio" name="giornoDisponibile" value="4" checked>
                <label for="gio">Giovedì</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="ven" name="giornoDisponibile" value="5" checked>
                <label for="ven">Venerdì</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="sab" name="giornoDisponibile" value="6" checked>
                <label for="sab">Sabato</label>
            </div>
            <div class="checkbox-container">
                <input type="checkbox" class="giorno-checkbox" id="dom" name="giornoDisponibile" value="7" checked>
                <label for="dom">Domenica</label>
            </div>
        </div><br/>

        <div id="filtro-slide-prezzi">
            <label for="range-prezzi"><strong>Tariffa oraria</strong>: max. <span id="prezzo">100€</span></label>
            <input type="range" id="range-prezzi" oninput="updateLabel(this.value)" min="1" max="100" value="100" step="1">
        </div><br/>

        <div id="filtro-sorting">
            <label for="sort"><strong>Ordina per</strong>: </label>
            <select name="sort" id="sort">
                <option value="" selected>-</option>
                <option value="Tutor.tariffa_oraria ASC">Prezzo crescente</option>
                <option value="Tutor.tariffa_oraria DESC">Prezzo decrescente</option>
                <option value="Tutor.ore_tutoring DESC">Esperienza</option>
                <option value="Tutor.recensione_media DESC">Valutazione</option>
            </select>
        </div><br/>

        <div id="filtro-recensioni">
            <label>Recensioni degli utenti:</label>
            <button class="rating-button" value="1"><img src="img/rating-1.png" alt="Valutazione da 1 stella in su"><span> in su</span></button><br/>
            <button class="rating-button" value="2"><img src="img/rating-2.png" alt="Valutazione da 2 stelle in su"><span> in su</span></button><br/>
            <button class="rating-button" value="3"><img src="img/rating-3.png" alt="Valutazione da 3 stelle in su"><span> in su</span></button><br/>
            <button class="rating-button" value="4"><img src="img/rating-4.png" alt="Valutazione da 4 stelle in su"><span> in su</span></button><br/>
        </div><br/>
    </div>

    <!-- Insieme delle card dei tutor -->
    <%if (tutorList.isEmpty()) {%>
    <div id="no-results-container">
        <img src="img/research-failed.png" alt="Ricerca fallita.">
        <p>Nessun insegnante offre lezioni<br>di <span><%=givenInput%></span>. :(</p>
    </div>

    <%} else {%>
    <div class="card-container" id="card-container-grid">
        <%for (Tutor t : tutorList) {%>
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
        <%if (tutorList.size() < 15) {%>
            <button style="display: none" id="show-more" class="standard-button" value="0">Visualizza più tutor</button>
        <%} else {%>
            <button id="show-more" class="standard-button" value="0">Visualizza più tutor</button>
        <%}%>
    </div>
    <%}%>
</div>

<script src="js/showTutors.js"></script>
</body>
</html>
