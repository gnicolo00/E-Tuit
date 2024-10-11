<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Ticket" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Studente> studenti = (ArrayList<Studente>) request.getAttribute("studenti");
    ArrayList<Tutor> tutors = (ArrayList<Tutor>) request.getAttribute("tutors");
    ArrayList<Studente> studentiBannati = (ArrayList<Studente>) request.getAttribute("studentiBannati");
    ArrayList<Tutor> tutorsBannati = (ArrayList<Tutor>) request.getAttribute("tutorsBannati");
    ArrayList<Ticket> ticketList = (ArrayList<Ticket>) request.getAttribute("ticketList");
    ArrayList<String> dateTickets = (ArrayList<String>) request.getAttribute("dateTickets");
    double guadagnoTot = (double) request.getAttribute("guadagnoTot");
    int numOreTutoringTot = (int) request.getAttribute("numOreTutoringTot");
    double recensMediaTot = (double) request.getAttribute("recensMediaTot");
    int numOrdiniTotali = (int) request.getAttribute("numOrdiniTotali");
    int numLezioniTotali = (int) request.getAttribute("numLezioniTotali");
    int numTutorTotali = (int) request.getAttribute("numTutorTotali");
    int numStudentiTotali = (int) request.getAttribute("numStudentiTotali");
%>

<html>
<head>
    <link rel="stylesheet" href="css/base.css" type="text/css">
    <link rel="stylesheet" href="css/admin.css" type="text/css">

    <title>Admin Data</title>
</head>
<body>
<%@ include file="header/header.jsp"%><br/>
<div id="admin-container">
    <h1 id="begin">Admin - Pagina di monitorazione</h1>

    <h2 id="user-list"></h2>
    <br/>
    <div class="table-wrapper">
        <div class="table-container">

        </div>
    </div>

    <div class="table-wrapper">
        <div class="table-container">
            <%if(tutors != null){%>
            <table class="user-admin-table">
                <tr><th colspan="7" class="table-header"><strong>Lista tutor</strong></th></tr>
                <%for(Tutor t : tutors){%>
                <tr>
                    <td><%=t.getEmailUtente()%></td>
                    <td><%=t.getNome() + " " + t.getCognome()%></td>
                    <td>
                        <form action="ban-and-sban" method="get" onsubmit="return validateEmailBan('tutorEmailBan')">
                            <input type="hidden" id="tutorEmailBan" name="emailUtenteBan" value="<%=t.getEmailUtente()%>">
                            <input class="ban-button change-button" type="submit" value="Banna">
                        </form>
                    </td>
                    <td>
                        <form action="change-card-descr-admin" id="change-card-descr-form" method="post" onsubmit="return validateChangeCardDescrAdmin()">
                            <input type="hidden" id="changeCardDescrEmail" name="changeCardDescrEmail" value="<%=t.getEmailUtente()%>">
                            <input type="hidden" id="newCardAdminDescription" name="newCardAdminDescription" value="Descrizione non valida.">
                            <input class="change-card-descr-button change-button" type="submit" value="Cambia descrizione card del tutor">
                        </form>
                    </td>
                    <td>
                        <form action="change-compl-descr-admin" id="change-compl-descr-form" method="post" onsubmit="return validateChangeCompleteDescrAdmin()">
                            <input type="hidden" id="changeComplDescrEmail" name="changeComplDescrEmail" value="<%=t.getEmailUtente()%>">
                            <input type="hidden" id="newCompleteAdminDescription" name="newCompleteAdminDescription" value="Descrizione non valida.">
                            <input class="change-compl-descr-button change-button" type="submit" value="Cambia descrizione completa del tutor">
                        </form>
                    </td>
                    <td>
                        <form action="change-name-admin" method="get" onsubmit="validateChangeNameTutor()">
                            <input type="hidden" id="changeNameTutorEmail" name="changeNameTutorEmail" value="<%=t.getEmailUtente()%>">
                            <input type="hidden" id="newFirstNameTutor" name="newFirstNameTutor" value="Nome">
                            <input type="hidden" id="newLastNameTutor" name="newLastNameTutor" value="Cognome">
                            <input class="change-name-button change-button" type="submit" value="Cambia nominativo dell'utente">
                        </form>
                    </td>
                    <td>
                        <form action="tutor-page" method="get">
                            <input type="hidden" name="tutorEmail" value="<%=t.getEmailUtente()%>">
                            <input class="see-tutorpage-button change-button" type="submit" value="Visualizza profilo del tutor">
                        </form>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>
        </div>
    </div>
    <br/><br/>
    <div class="table-wrapper">
        <div class="table-container">
            <%if(studenti != null){%>
            <table class="user-admin-table">
                <tr><th colspan="4" class="table-header"><strong>Lista studenti</strong></th></tr>
                <%for(Studente s : studenti){%>
                <tr>
                    <td><%=s.getEmailUtente()%></td>
                    <td><%=s.getNome() + " " + s.getCognome()%></td>
                    <td>
                        <form action="ban-and-sban" method="get" onsubmit="return validateEmailBan('studentEmailBan')">
                            <input type="hidden" id="studentEmailBan" name="emailUtenteBan" value="<%=s.getEmailUtente()%>">
                            <input class="ban-button change-button" type="submit" value="Banna">
                        </form>
                    </td>
                    <td>
                        <form action="change-name-admin" method="get" onsubmit="validateChangeNameStudent()">
                            <input type="hidden" id="changeNameStudentEmail" name="changeNameStudentEmail" value="<%=s.getEmailUtente()%>">
                            <input type="hidden" id="newFirstNameStudent" name="newFirstNameStudent" value="Nome">
                            <input type="hidden" id="newLastNameStudent" name="newLastNameStudent" value="Cognome">
                            <input class="change-name-button change-button" type="submit" value="Cambia nominativo dell'utente">
                        </form>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>
        </div>
    </div>

    <br/><br/>

    <h2 id="banned-user-list"></h2>

    <div class="table-wrapper">
        <div class="table-container">
            <% if(studentiBannati != null || tutorsBannati != null){ %>
            <table class="user-admin-table" id="banned-table">
                <tr class="banned-row"><th colspan="3" class="table-header"><strong>Lista utenti bannati</strong></th></tr>
                <%if(studentiBannati != null){%>
                <%for(Studente sb : studentiBannati){%>
                <tr class="banned-row">
                    <td><%=sb.getEmailUtente()%></td>
                    <td><%=sb.getNome() + " " + sb.getCognome()%>, studente</td>
                    <td>
                        <form action="ban-and-sban" method="get" onsubmit="return validateEmailBan('studentEmailSban')">
                            <input type="hidden" id="studentEmailSban" name="emailUtenteBan" value="<%=sb.getEmailUtente()%>">
                            <input class="sban-button change-button" type="submit" value="Rimuovi ban">
                        </form>
                    </td>
                </tr>
                <%}%>
                <%}%>
                <%if(tutorsBannati != null){%>
                <%for(Tutor tb : tutorsBannati){%>
                <tr class="banned-row">
                    <td><%=tb.getEmailUtente()%></td>
                    <td><%=tb.getNome() + " " + tb.getCognome()%>, tutor</td>
                    <td>
                        <form action="ban-and-sban" method="get" onsubmit="return validateEmailBan('tutorEmailSban')">
                            <input type="hidden" id="tutorEmailSban" name="emailUtenteBan" value="<%=tb.getEmailUtente()%>">
                            <input class="sban-button change-button" type="submit" value="Rimuovi ban">
                        </form>
                    </td>
                </tr>
                <%}%>
                <%}%>
            </table>
            <%}%>
        </div>
    </div>


    <br/>

    <div id="ticket-div">
        <br/><h2 id="ticket-list">Tickets del sito:</h2><br/>
        <%if(ticketList != null){%>
        <div class="tickets-container">
            <%for(int i=0; i<ticketList.size(); i++){
                Ticket t = ticketList.get(i); %>
            <div class="ticket-card">
                <p class="ticket-id"> Ticket ID: <%=t.getID()%> </p>
                <p> Inviato il: <%=dateTickets.get(i)%> </p>
                <p> Da: <%=t.getEmailUtente()%> </p>
                <p> Tipo di richiesta: <%=t.getTipoRichiesta()%> </p>
                <p> Oggetto: <%=t.getOggetto()%> </p>
                <p> Descrizione: <%=t.getDescrizione()%> </p>
            </div>
            <%}%>
        </div>
        <%}%>
    </div>

    <div id="info-container">
        <h2 id="website-info">Informazioni sul sito</h2>
        <p>Guadagno ottenuto dal sito in totale: <%=guadagnoTot%>€ </p>
        <p>Numero di ore di tutoring svolte finora:<%=numOreTutoringTot%></p>
        <p>Recensione media del sito in base alle recensioni sugli insegnanti: </span><%=recensMediaTot%></p>
        <p>Numero di ordini effettuati dal sito finora: <%=numOrdiniTotali%></p>
        <p>Numero di lezioni prenotate sul sito finora (comprese quelle non appartenenti agli ordini): </span><%=numLezioniTotali%></p>
        <p>Numero totale di utenti registrati: <%=numStudentiTotali + numTutorTotali%>,
            di cui <%=numStudentiTotali%> studenti e <%=numTutorTotali%> tutor. </p>
    </div>

    <br/><br/>
    <a href="#begin">
        <button class="standard-button">Torna sopra</button>
    </a>
</div>

</body>
</html>