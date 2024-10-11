<%@ page import="model.Studente" %>
<%@ page import="model.Tutor" %>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  HttpSession sessionHeader = request.getSession();
  String loginError = (String) sessionHeader.getAttribute("login-error");
  String registrationError = (String) sessionHeader.getAttribute("registration-error");
  Studente studenteHeader = (Studente) sessionHeader.getAttribute("studente");
  Tutor tutorHeader = (Tutor) sessionHeader.getAttribute("tutor");
  Studente admin = (Studente) sessionHeader.getAttribute("admin");
  String userEmailHeader = "";
  if(tutorHeader != null)
    userEmailHeader = tutorHeader.getEmailUtente();
  else if(studenteHeader != null)
    userEmailHeader = studenteHeader.getEmailUtente();
  String imagePathHeader = "propics_by_email/" + userEmailHeader.replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
  File imageFileHeader = new File(request.getServletContext().getRealPath(imagePathHeader));
  boolean profileImageExistsHeader = imageFileHeader.exists();
%>

<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1"/>
  <link rel="stylesheet" href="../css/base.css" type="text/css">
  <link rel="icon" type="image/png" href="img/logo-purple.png">
  <script src="js/validation.js" type="text/javascript"></script>
  <script src="js/display.js" type="text/javascript"></script>
  <script src="js/jquery.js" type="text/javascript"></script>
  <title>Header</title>
</head>
<body>
  <%@ include file="login.jsp"%>
  <%@ include file="registration.jsp"%>

  <nav>
    <ul>
      <li id="logo-li">
        <form action="home-servlet" method="get">
          <button id="logo" type="submit"></button>
        </form>
      </li>
      <li><a href="about-us.jsp">Chi siamo</a></li>
      <li><a href="contact-us.jsp">Contattaci</a></li>
      <%if(studenteHeader == null && tutorHeader == null && admin == null) {%>
      <li class="cart-navbar"><button class="cart-button" onclick="location.href='cart.jsp'"></button></li>
      <li id="login-navbar"><button class="standard-button" id="login-button" onclick="openLoginAndRegistrationPopup('login-popup'); closeLoginAndRegistrationPopup('registration-popup')">Login</button></li>
      <li id="registration-navbar"><button class="standard-button" id="registration-button" onclick="openLoginAndRegistrationPopup('registration-popup'); closeLoginAndRegistrationPopup('login-popup')">Registrati</button></li>
      <%} else { %>
          <%if(tutorHeader == null && admin == null) {%>
            <li class="cart-navbar"><button class="cart-button" onclick="location.href='cart.jsp'"></button></li>
          <%}%>
          <%if (!profileImageExistsHeader) {%>
            <li id="propic-navbar"><img src="img/default-user-icon.jpg" alt="Immagine del profilo." class="profile-icon" onmouseenter="displayPersonalList('show')" onmouseleave="displayPersonalList('hide')"></li>
          <%} else {%>
            <li id="propic-navbar"><img src="<%=imagePathHeader%>" alt="Immagine del profilo." class="profile-icon" onmouseenter="displayPersonalList('show')" onmouseleave="displayPersonalList('hide')"></li>
          <%}%>
      <%}%>
    </ul>
  </nav>
  <div id="personal-list" onmouseenter="displayPersonalList('stop')" onmouseleave="displayPersonalList('hide')">
    <ul>
      <li>
        <%if (studenteHeader != null || tutorHeader != null) {%>
        <form action="profile-servlet" method="get">
          <input class="list-button" type="submit" value="Profilo">
        </form>
        <%}%>
      </li>
      <li>
        <% if (studenteHeader != null) {%>
          <form action="orders-servlet" method="get">
            <input class="list-button" type="submit" value="I miei ordini">
          </form>
        <%} else if (admin != null) {%>
        <form action="admin-data" method="get">
          <input class="list-button" type="submit" value="Dati del sito">
        </form>
        <%}%>
      </li>
      <hr>
      <li>
        <form action="logout-servlet" method="get">
          <input class="list-button" type="submit" value="Logout">
        </form>
      </li>
    </ul>
  </div>
</body>
</html>
