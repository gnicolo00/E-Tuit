package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Studente;
import model.Ticket;
import model.TicketDAO;
import model.Tutor;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "TicketServlet", value = "/ticket-servlet")
public class TicketServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Recupero dei campi
        Studente studenteTicket = (Studente) request.getSession().getAttribute("studente");
        Tutor tutorTicket = (Tutor) request.getSession().getAttribute("tutor");
        String emailSender = "";
        if(studenteTicket != null)
            emailSender = studenteTicket.getEmailUtente();
        else if(tutorTicket != null)
            emailSender = tutorTicket.getEmailUtente();

        String type = request.getParameter("request-type");
        String object = request.getParameter("object");
        String description = request.getParameter("description");
        String destination;

        // Validazione della scelta del tipo di problema del ticket
        if (    type == null || (
                !type.equals("tech-issues") &&
                !type.equals("account-issues") &&
                !type.equals("payment-issues") &&
                !type.equals("report-user") &&
                !type.equals("ban-issues") &&
                !type.equals("generic-question") &&
                !type.equals("other")) ||

                object == null ||
                object.isBlank() ||
                object.length() > 100 ||

                description == null ||
                description.isBlank() ||
                description.length() > 1500) {
            destination = "/WEB-INF/errors/error500.jsp";
        }
        else {// Se i dati sono corretti, si procedere alla creazione del ticket e al suo salvataggio nel database
            Ticket ticket = new Ticket();

            ticket.setTipoRichiesta(type);
            ticket.setOggetto(object);
            ticket.setDescrizione(description);
            ticket.setDataInvio(new Date());
            ticket.setEmailUtente(emailSender);
            TicketDAO ticketDAO = new TicketDAO();
            ticketDAO.doSave(ticket);

            destination = "/WEB-INF/contact-thank-you.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
    //TODO: fare in modo che venga aggiunto ticket al db, prende dati da contact-us.jsp
    //todo: controllare per i parametri messi che non siano vuoti (isempty)
    //todo: parametri del ticket da controllare con javascript
}
