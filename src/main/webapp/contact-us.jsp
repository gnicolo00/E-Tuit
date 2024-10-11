<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/contact-us.css" type="text/css">
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/actions.js" type="text/javascript"></script>
    <title>Invia una richiesta | E-Tuit.it</title>
</head>
<body>
    <%@ include file="WEB-INF/header/header.jsp"%>

    <%if (studenteHeader == null && tutorHeader == null && admin == null) {%>
        <div id="login-or-register-container">
            <h1>Autenticati!</h1>
            <p>È necessario un account per poter inviare un Ticket al nostro staff.</p>
            <p id="login-or-register">Accedi/Registrati per permetterci di aiutarti!</p>
            <button class="standard-button" id="login-button-contact">Accedi</button>
            <button class="standard-button" id="registration-button-contact">Registrati</button>
        </div>
    <%} else {%>
    <form action="ticket-servlet" method="get">
        <div id="ticket-container">
            <h1>Invia una richiesta</h1>
            <p id="ticket-message">Che siano problemi tecnici o altro, siamo pronti ad aiutarti!<br>Invia un ticket! Se non verrà smarrito in un flusso di bit, ti risponderemo al più presto.</p>
            <p class="info-title" id="details">DETTAGLI</p>
            <hr>
            <div class="ticket-sub-container">
                <label for="request-type" class="info-title">SCEGLI IL TIPO DI RICHIESTA</label>
                <select id="request-type" name="request-type" class="text-container" required>
                    <option value="">-</option>
                    <option value="tech-issues">Problemi tecnici</option>
                    <option value="account-issues">Recupera il mio account</option>
                    <option value="payment-issues">Pagamenti e transazioni</option>
                    <option value="report-user">Segnala un utente</option>
                    <option value="ban-issues">Discussione su una sospensione, ban o limitazioni all'account personale</option>
                    <option value="generic-question">Domanda generica</option>
                    <option value="other">Altro</option>
                </select>
            </div>
            <div class="ticket-sub-container">
                <label for="object" class="info-title">OGGETTO</label>
                <input type="text" id="object" name="object" class="text-container" maxlength="100" required>
            </div>
            <div class="ticket-sub-container">
                <label for="description" class="info-title">DESCRIZIONE</label>
                <textarea id="description" name="description" class="text-container" rows="5" cols="50" placeholder="Descrivi il tuo problema qui..." maxlength="1500" required></textarea>
            </div>
            <div id="file-drop-container">
                <label id="attachments" class="info-title">ALLEGATI</label>
                <div id="file-drop-area">
                    <input type="file" id="file-drop" name="file-drop" multiple>
                    <label for="file-drop" class="info-title"><span>AGGIUNGI OPPURE TRASCINA UN FILE QUI</span></label>
                </div>
            </div>
            <button id="send-request-button" class="standard-button">Invia</button>
        </div>
    </form>
    <%}%>
</body>
</html>