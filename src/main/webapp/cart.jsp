<%@ page import="model.Carrello" %>
<%@ page import="model.Lezione" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/cart.css" type="text/css">
    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="js/display.js" type="text/javascript"></script>
    <script src="js/actions.js" type="text/javascript"></script>
    <title>Carrello | E-Tuit.it</title>
</head>

<%
    Carrello carrello = (Carrello) request.getSession().getAttribute("cart");
    String unauthenticatedUserError = (String) request.getSession().getAttribute("unauthenticated-user-error");
    int carico = carrello.getCarico();
    String lezionx;
    if (carico == 1)
        lezionx = "lezione";
    else
        lezionx = "lezioni";
%>

<body>
    <%@ include file="WEB-INF/header/header.jsp"%>
    <div id="cart-page-container">
        <div id="cart-container">
            <h1>Carrello</h1>
            <p id="cart-quantity"><span id="quantity"><%=carico%></span> <%=lezionx%> nel carrello</p>
            <%if(carico == 0) {%>
            <hr>
            <p id="empty-cart">Il tuo carrello è vuoto. Conosci altri tutor e prenota una lezione che ti interessa!</p>
            <form action="show-tutors" method="get">
                <input type="submit" id="show-tutors-cart" class="standard-button" value="Visualizza i Tutor">
            </form>
            <%} else {%>
            <ul class="lesson-list">
                <%for(Lezione lezione : carrello.getListaLezioni()) {%>
                <hr id="lesson<%=lezione.getID()%>-separator">
                <li class="cart-lesson" id="lesson<%=lezione.getID()%>">
                    <%
                        String imagePath = "propics_by_email/" + lezione.getEmailTutor().replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
                        File imageFile = new File(request.getServletContext().getRealPath(imagePath));
                        boolean profileImageExists = imageFile.exists();
                    %>
                    <%if (!profileImageExists) {%>
                    <img src="img/default-user-icon.jpg" alt="Immagine del profilo." class="cart-image">
                    <%} else {%>
                    <img src="<%=imagePath%>" alt="Immagine del profilo." class="cart-image">
                    <%}%>

                    <p class="lesson-info">Lezione di <span class="lesson-subject"><%=lezione.getMateria().substring(0,1).toUpperCase() + lezione.getMateria().substring(1)%></span><br>
                        <span class="lesson-tutor">Di <%=lezione.getNomeTutor()%></span></p>
                    <p class="total-hours"><%=lezione.getOre()%> ore totali</p>

                    <button onclick="removeLesson(<%=lezione.getID()%>, <%=lezione.getPrezzo()%>)">Rimuovi</button>

                    <div class="lesson-total"><%=lezione.getPrezzo()%> €</div>
                </li>
                <%}%>
            </ul>

            <%if(unauthenticatedUserError != null) {%>
            <p style="color: red; font-size: 15px;"><%=unauthenticatedUserError%></p>
            <% request.getSession().removeAttribute("unauthenticated-user-error"); %>
            <%}%>
            <%}%>
        </div>

        <div id="payment-container">
            <p id="cart-total">Totale:<br><span id="total"><%=carrello.getTotale()%> €</span></p>
            <form action="payment-servlet" method="get" id="paymentButton">
                <input type="submit" value="Procedi al pagamento">
            </form>
            <hr>
        </div>
    </div>

</body>
</html>
