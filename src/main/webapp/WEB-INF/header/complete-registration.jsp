<%@ page import="model.Tutor" %>
<!-- pagina dedicata al form per il completamento del profilo del tutor-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    //potrebbero esser commentati, devo valutare se servono o no
    Tutor tutor2 = (Tutor) session.getAttribute("tutor");
    session.setAttribute("tutor", tutor2);
%>

<html>
    <head>
        <title>Completa la tua registrazione!</title>
        <link rel="stylesheet" href="css/base.css" type="text/css">
        <link rel="stylesheet" href="css/complete-registration.css" type="text/css">
        <link rel="stylesheet" href="css/profile.css" type="text/css">
        <link rel="stylesheet" href="css/contact-us.css" type="text/css">
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/validation.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/js/actions.js"></script>
    </head>
    <body>
    <%@ include file="../header/header.jsp"%>
    <div id="complete-reg-bg">
        <div id="complete-reg-container">
            <form id="complete-registration-form" action="complete-registration" method="get" onsubmit="return validateCompleteRegistration()">
                <h2>Ultima i tuoi dettagli da tutor!</h2>
                <div id="titoli-div">
                    <p>Inserisci il tuo titoli/i tuoi titoli di studio: </p>
                    <input type="text" name="titolo" class="text-field" placeholder="Laurea in Informatica" required>
                    <button onclick="aggiungiTitolo()" class="standard-button add-button">+</button>
                </div>
                <div id="settori-div">
                    <p>Inserisci i settori in cui insegni: </p>
                    <input type="text" name="settore" class="text-field" placeholder="Informatica" required>
                    <button onclick="aggiungiSettore()" class="standard-button add-button">+</button>
                </div>
                <div id="tar-oraria-div">
                    <p>Inserisci la tua tariffa oraria: </p>
                    <input type="range" id="tariffaOraria" name="tariffaOraria" oninput="updateLabel(this.value)" min="1" max="100" value="5" required>
                    <label for="tariffaOraria" id="prezzo">5€</label>
                </div>
                <div id="giorni-disp-div">
                    <p id="giorni-par">Inserisci i tuoi giorni di disponibilità: </p>
                    <div id="giorni-div">
                        <p><input type="checkbox" id="lunedì" name="giornoDisponibile" value="lunedì"><label for="lunedì">Lunedì</label></p>
                        <p><input type="checkbox" id="martedì" name="giornoDisponibile" value="martedì"><label for="martedì">Martedì</label></p>
                        <p><input type="checkbox" id="mercoledì" name="giornoDisponibile" value="mercoledì"><label for="mercoledì">Mercoledì</label></p>
                        <p><input type="checkbox" id="giovedì" name="giornoDisponibile" value="giovedì"><label for="giovedì">Giovedì</label></p>
                        <p><input type="checkbox" id="venerdì" name="giornoDisponibile" value="venerdì"><label for="venerdì">Venerdì</label></p>
                        <p><input type="checkbox" id="sabato" name="giornoDisponibile" value="sabato"><label for="sabato">Sabato</label></p>
                        <p><input type="checkbox" id="domenica" name="giornoDisponibile" value="domenica"><label for="domenica">Domenica</label></p>
                    </div>
                </div>
                <div id="descr-card-div">
                    <p>Inserisci la descrizione per la tua card che comparirà nel sito: </p>
                    <textarea name="descrizione-card" id="descrizione-card" class="text-container" minlength="50" maxlength="150" placeholder="Professore di Ingegneria Aerospaziale presso ..." required></textarea>
                </div>
                <div id="descr-compl-div">
                    <p>Inserisci la descrizione completa per la tua pagina: </p>
                    <textarea name="descrizione-completa" id="descrizione-completa" class="text-container" minlength="200" maxlength="1500" placeholder="Altro ..." required></textarea>
                </div>
                <input type="submit" value="Completa la registrazione" class="standard-button">
            </form>
        </div>
    </div>
        <%@ include file="../footer/footer.jsp"%>

        <script>
            function aggiungiTitolo(){
                const elem1 = document.createElement("input");
                elem1.type = "text";
                elem1.name = "titolo";
                elem1.className = "text-field";
                elem1.placeholder = "Laurea in Informatica";
                elem1.required = true;
                document.getElementById("titoli-div").appendChild(elem1);
                const elem2 = document.createElement("br");
                document.getElementById("titoli-div").appendChild(elem2);
            }

            function aggiungiSettore() {
                const elem1 = document.createElement("input");
                elem1.type = "text";
                elem1.name = "settore";
                elem1.placeholder = "Informatica";
                elem1.className = "text-field";
                elem1.required = true;
                document.getElementById("settori-div").appendChild(elem1);
                const elem2 = document.createElement("br");
                document.getElementById("settori-div").appendChild(elem2);
            }
        </script>

    </body>
</html>