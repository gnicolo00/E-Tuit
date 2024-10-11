<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registrazione</title>
    <script src="js/validation.js" type="text/javascript"></script>
</head>
<body>
  <div id="registration-popup" class="header-popup">
    <button class="close-popup" onclick="closeLoginAndRegistrationPopup('registration-popup')">&times;</button>
    <h2 id="popup-title">Registrazione</h2>
    <form id="registration-form" action="registration-servlet" method="post" onsubmit="return validateRegistration()">
      <div>
        <label for="first-name">Nome</label>
        <input type="text" id="first-name" class="popup-text" name="first-name" minlength="1" maxlength="50" placeholder="Mario" required>
      </div>
      <div>
        <label for="last-name">Cognome</label>
        <input type="text" id="last-name" class="popup-text" name="last-name" minlength="1" maxlength="50" placeholder="Rossi" required>
      </div>
      <div>
        <label>Sesso: </label>
        <input type="radio" id="male" name="gender" value="M" checked required>
        <label for="male">Maschio</label>
        <input type="radio" id="female" name="gender" value="F" required>
        <label for="female">Femmina</label><br/><br/>
        <label>Tipologia: </label>
        <input type="radio" id="student" name="type" value="student" checked required>
        <label for="student">Studente</label>
        <input type="radio" id="tutor" name="type" value="tutor" required>
        <label for="tutor">Tutor</label>
      </div>
      <div>
        <label for="birthday">Data di nascita: </label>
        <input type="date" id="birthday" name="birthday" required>
      </div>
      <div>
        <label for="email-registration">Email</label>
        <input type="text" id="email-registration" class="popup-text" name="email-registration" maxlength="50" placeholder="mariorossi@gmail.com" required>
      </div>
      <%if(registrationError != null) {%>
          <p style="color: red; font-size: 15px;"><%=registrationError%></p>
          <script>openLoginAndRegistrationPopup('registration-popup')</script>
      <%  sessionHeader.removeAttribute("registration-error");
      }%>
      <div>
        <label for="password-registration">Password</label>
        <input type="password" id="password-registration" class="popup-text" name="password-registration" maxlength="20" placeholder="La tua password" required>
      </div>
      <div id="declaration-container">
        <input type="checkbox" id="declaration" required>
        <label for="declaration">&nbsp;Dichiaro di aver letto e accettato integralmente le <strong>Condizioni generali d'uso</strong>.</label>
      </div>
      <input type="submit" value="Registrati" class="standard-button">
    </form>
  </div>
</body>
</html>