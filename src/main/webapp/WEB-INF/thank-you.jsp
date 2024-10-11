<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Grazie per l'acquisto! | E-Tuit.it</title>
    <link rel="stylesheet" href="css/base.css" type="text/css">
</head>
<body>
    <%@ include file="header/header.jsp"%><br/><br/>

    <h2>Grazie!</h2>
    <h2>L'ordine è stato effettuato!</h2>
    <h3>Il/i tutor ti contatterà/contatteranno il prima possibile per poter ultimare i dettagli della prenotazione. :)</h3>

    <form action="show-tutors" method="get">
      <input type="submit" value="Visualizza i Tutor" class="standard-button">
    </form>

</body>
</html>
