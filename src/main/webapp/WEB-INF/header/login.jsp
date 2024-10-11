<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Login</title>
  <script src="js/validation.js" type="text/javascript"></script>
<body>
  <div id="login-popup" class="header-popup">
    <button class="close-popup" onclick="closeLoginAndRegistrationPopup('login-popup')">&times;</button>
    <h2 id="popup-title">Log&nbsp;in</h2>
    <form id="login-form" action="login-servlet" method="post" onsubmit="return validateLogin()">
      <div>
        <label for="email-login">Email</label>
        <input type="text" id="email-login" class="popup-text" name="email-login" maxlength="50" placeholder="mariorossi@gmail.com" required>
      </div>
      <div>
        <label for="password-login">Password</label>
        <input type="password" id="password-login" class="popup-text" name="password-login" maxlength="20" placeholder="La tua password" required>
      </div>
      <%if(loginError != null) {%>
          <p style="color: red; font-size: 15px;"><%=loginError%></p>
          <script>openLoginAndRegistrationPopup('login-popup')</script>
      <%  sessionHeader.removeAttribute("login-error");
      }%>
      <input type="submit" value="Accedi" class="standard-button">
    </form>
  </div>
</body>
</html>
