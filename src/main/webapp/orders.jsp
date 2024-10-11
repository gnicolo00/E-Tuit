<%@ page import="java.util.ArrayList" %>
<%@ page import="model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Ordine> orders = (ArrayList<Ordine>) request.getSession().getAttribute("orders");
    ArrayList<String> dates = (ArrayList<String>) request.getAttribute("dateVisualizzabili");
    int size = orders.size();
    String ordinx;
    if (size == 1)
        ordinx = "ordine effettuato";
    else
        ordinx = "ordini effettuati";
%>

<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/cart.css" type="text/css">
    <link rel="stylesheet" href="css/orders.css" type="text/css">
    <title>I miei ordini | E-Tuit.it</title>
</head>
<body>
    <%@ include file="WEB-INF/header/header.jsp"%>

    <div id="orders-container">
        <h1>I miei ordini</h1>
        <p id="orders-quantity"><%=size + " " + ordinx%></p>
        <%if (size == 0) {%>
            <hr>
            <p id="no-orders">Sembra che tu non abbia effettuato alcun ordine.<br><span>Esplora i nostri tutor</span> e prenota una lezione che ti interessa!</p>
            <form action="show-tutors" method="get">
                <input type="submit" id="show-tutors-orders" class="standard-button" value="Visualizza i Tutor">
            </form>
        <%}%>
        <%for (int i = 0; i < size; i++) {%>
            <br>
            <h2 class="order-date"><%=dates.get(i)%></h2>
            <p class="order-id">ID Ordine: <%=orders.get(i).getID()%></p>
            <p class="order-size"><%=orders.get(i).getNumLezioni()%> lezioni prenotate</p>
            <div id="order-container">
                <ul>
                    <%for (Lezione lezione : orders.get(i).getLezioniPrenotate()) {%>
                        <hr>
                        <li class="order-lesson">
                            <%
                                String imagePath = "propics_by_email/" + lezione.getEmailTutor().replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
                                File imageFile = new File(request.getServletContext().getRealPath(imagePath));
                                boolean profileImageExists = imageFile.exists();
                            %>
                            <%if (!profileImageExists) {%>
                            <img src="img/default-user-icon.jpg" alt="Immagine del profilo." class="order-image">
                            <%} else {%>
                            <img src="<%=imagePath%>" alt="Immagine del profilo." class="order-image">
                            <%}%>

                            <p class="lesson-info">Lezione di <span class="lesson-subject"><%=lezione.getMateria().substring(0,1).toUpperCase() + lezione.getMateria().substring(1)%></span><br>
                                <span class="lesson-tutor">Di <%=lezione.getNomeTutor()%></span></p>
                            <p class="total-hours"><%=lezione.getOre()%> ore totali</p>

                            <div class="lesson-total"><%=lezione.getPrezzo()%> €</div>
                        </li>
                    <%}%>
                </ul>
                <p id="order-total">Totale: <span><%=orders.get(i).getPrezzoTotale()%> €</span></p>
            </div>
        <%}%>
    </div>
</body>
</html>