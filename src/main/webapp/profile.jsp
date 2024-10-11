<%@ page import="java.io.File" %>
<%@ page import="model.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
  Tutor tutorProfile = (Tutor) request.getSession().getAttribute("tutor");
  String numOrdiniStudente = null, numLezioniPrenStudente = null;
  Studente studenteProfile = (Studente) request.getSession().getAttribute("studente");
  if(studenteProfile != null) {
    numOrdiniStudente = (String) request.getAttribute("numOrdiniStudente");
    numLezioniPrenStudente = (String) request.getAttribute("numLezioniPrenStudente");
  }
  String userEmail = (String) request.getAttribute("email");
  String nome = (String) request.getAttribute("nome");
  String cognome = (String) request.getAttribute("cognome");
  String ddn = (String) request.getAttribute("ddn");
  String imagePath = "propics_by_email/" + userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
  File imageFile = new File(request.getServletContext().getRealPath(imagePath));
  boolean profileImageExists = imageFile.exists();
%>

<head>
  <title>Profilo | E-Tuit.it</title>
  <link rel="stylesheet" href="css/base.css" type="text/css">
  <link rel="stylesheet" href="css/profile.css" type="text/css">
  <link rel="stylesheet" href="css/contact-us.css" type="text/css">
  <script src="js/jquery.js" type="text/javascript"></script>
  <script src="js/actions.js" type="text/javascript"></script>
  <script src="js/validation.js" type="text/javascript"></script>
  <script src="js/display.js" type="text/javascript"></script>
</head>

<body>
<%@ include file="WEB-INF/header/header.jsp"%><br/>
<div class="profile-background">
  <div class="profile-container">
    <div class="left">
      <div class="profile-img-container">
        <div class="profile-image">
          <%if (!profileImageExists) {%>
          <img class="profile-image" src="img/default-user-icon.jpg" alt="Immagine del profilo.">
          <%} else {%>
          <img class="profile-image" src="<%=imagePath%>" alt="Immagine del profilo.">
          <%}%>
        </div>
      </div>

      <div id="change-img-div">
        <form action="image-servlet" method="post" enctype="multipart/form-data">
          <label for="image-file" id="image-file-label" class="standard-button">Modifica Avatar</label>
          <input type="file" id="image-file" name="file">
          <input type="submit" id="confirm-image" class="standard-button" value="Conferma" disabled>
        </form>
      </div>

      <%if(tutorProfile != null){%>
        <div id="descr-card-container">
          <p><span class="profile-title">La mia descrizione nella card</span><br><%=tutorProfile.getDescrizioneCard()%></p>
          <button onclick="changeCardDescr()" id="change-card-descr-button" class="standard-button">Cambia la descrizione della tua card</button>
          <form action="change-card-descr" id="change-card-descr-form" method="post" onsubmit="return validateChangeCardDescr()">
            <textarea name="newCardDescription" id="newCardDescription" class="text-container" minlength="50" maxlength="150" placeholder="Nuova descrizione card..." required></textarea>
            <input id="new-card-descr-submit" type="submit" value="Cambia" class="standard-button">
          </form>
        </div>

        <div id="descr-compl-container">
          <p><span class="profile-title">La mia descrizione completa</span><br><%=tutorProfile.getDescrizioneCompleta()%></p>
          <button onclick="changeComplDescr()" id="change-compl-descr-button" class="standard-button">Cambia la tua descrizione completa</button>
          <form action="change-complete-descr" id="change-compl-descr-form" method="post" onsubmit="return validateChangeCompleteDescr()">
            <textarea name="newCompleteDescription" class="text-container" id="newCompleteDescription" minlength="50" maxlength="1500" placeholder="Nuova descrizione card..." required></textarea>
            <input id="new-compl-descr-submit" type="submit" value="Cambia" class="standard-button">
          </form>
        </div>
      <%}%>
    </div>
    <hr id="line">
    <div class="right">
      <p>Il profilo di <strong><span class="profile-title"><%=nome + " " + cognome%></span></strong></p>
      <button onclick="changeName()" id="change-name-button" class="standard-button">Cambia il tuo nominativo</button>
      <form action="change-name" id="change-name-form" method="get" onsubmit="return validateChangeName()">
        <input type="hidden" id="changeNameEmail" name="userEmail" value="<%=userEmail%>">
        <input type="text" id="newFirstName" name="newFirstName" minlength="1" maxlength="50" value="<%=nome%>" required>
        <input type="text" id="newLastName" name="newLastName" minlength="1" maxlength="50" value="<%=cognome%>" required>
        <input type="submit" id="new-name-submit" class="standard-button" value="Cambia">
      </form>

      <div id="email-bday-div">
        <p><span class="profile-title">La mia email:</span> <%=userEmail%></p>
        <p><span class="profile-title">La mia data di nascita:</span> <%=ddn%></p>
      </div>

      <%if(studenteProfile != null){%>
      <div id="student-info-div">
        <p><span class="profile-title">Numero di ordini effettuati da te in questo sito:</span> <%=numOrdiniStudente%>.</p>
        <p><span class="profile-title">Numero di lezioni prenotate da te in questo sito:</span> <%=numLezioniPrenStudente%>.</p>
      </div>
      <%}%>

      <%if(tutorProfile != null){%>
      <div id="other-info-container">
        <p><span class="profile-title">Recensione media che gli utenti hanno espresso su di me:</span> <%=tutorProfile.getRecensioneMedia()%>.</p>
        <p><span class="profile-title">Numero di tutoring che ho svolto:</span> <%=tutorProfile.getOreTutoring()%>. </p>
        <p><span class="profile-title">Guadagno totale che ho raccolto finora:</span> <%=tutorProfile.getGuadagno()%> €.</p>
      </div>

      <div id="t-oraria-container">
        <p><span class="profile-title">Tariffa oraria:</span> <%=tutorProfile.getTariffaOraria()%> €</p>
        <form action="change-hours-rate" id="change-hrate-form" method="get" onsubmit="return validateChangeHRate()">
          <input type="number" id="newTariffaOraria" name="newTariffaOraria" min="1" max="100" value="<%=tutorProfile.getTariffaOraria()%>" required>
          <input type="submit" id="new-tariffa-oraria-button" value="Cambia" class="standard-button">
        </form>
        <button onclick="changeHourlyRate()" id="change-hrate-button" class="standard-button">Cambia la tua tariffa oraria</button>
      </div>

      <div id="titoli-container">
        <p class="caption-list">Lista dei miei titoli</p>
        <%for(String titolo : tutorProfile.getListaTitoli()){%>
        <p><%=titolo%></p>
        <%}%>
      </div>

      <div id="settori-container">
        <p class="caption-list">Lista dei miei settori</p>
        <%for(String settore : tutorProfile.getArraySettori()){%>
        <p><%=settore.substring(0, 1).toUpperCase() + settore.substring(1)%></p>
        <%}%>
      </div>

      <div id="giorni-container">
        <p class="caption-list">Lista dei miei giorni di disponibilità</p>
        <%for(String giorno : tutorProfile.getListaGiorni()){%>
        <p><%=giorno%></p>
        <%}%>
        <hr id="giorni-separator">
        <button onclick="changeDays()" id="change-days-button" class="standard-button">Cambia i giorni di disponibilità</button>
        <form action="change-days" id="change-days-form" method="get" onsubmit="return validateChangeDays()">
          <div id="giorni-checklist">
            <p><input type="checkbox" id="newLunedì" name="nuovoGiornoDisponibile" value="lunedì"><label for="newLunedì">Lunedì</label></p>
            <p><input type="checkbox" id="newMartedì" name="nuovoGiornoDisponibile" value="martedì"><label for="newMartedì">Martedì</label></p>
            <p><input type="checkbox" id="newMercoledì" name="nuovoGiornoDisponibile" value="mercoledì"><label for="newMercoledì">Mercoledì</label></p>
            <p><input type="checkbox" id="newGiovedì" name="nuovoGiornoDisponibile" value="giovedì"><label for="newGiovedì">Giovedì</label></p>
            <p><input type="checkbox" id="newVenerdì" name="nuovoGiornoDisponibile" value="venerdì"><label for="newVenerdì">Venerdì</label></p>
            <p><input type="checkbox" id="newSabato" name="nuovoGiornoDisponibile" value="sabato"><label for="newSabato">Sabato</label></p>
            <p><input type="checkbox" id="newDomenica" name="nuovoGiornoDisponibile" value="domenica"><label for="newDomenica">Domenica</label></p>
            <p><input type="submit" id="send-change-days-button" class="standard-button" value="Cambia">
          </div>
        </form>
      </div>
      <%}%>
    </div>
  </div>
</div>
</body>
</html>