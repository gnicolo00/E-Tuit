package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@WebServlet(name = "ProfileServlet", value = "/profile-servlet")
public class ProfileServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Tutor tutor = (Tutor) request.getSession().getAttribute("tutor");
        Studente studente = (Studente) request.getSession().getAttribute("studente");
        String email = "", nome = "", cognome = "", ddn = "", numOrdini, numLezioniPrenStudente;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        OrdineDAO ordineDAO = new OrdineDAO();
        TicketDAO ticketDAO = new TicketDAO();
        LezioneDAO lezioneDAO = new LezioneDAO();

        if(tutor != null){
            email = tutor.getEmailUtente();
            nome = tutor.getNome();
            cognome = tutor.getCognome();
            ddn = dateFormat.format(tutor.getDataDiNascita());
        }
        if(studente != null){
            email = studente.getEmailUtente();
            nome = studente.getNome();
            cognome = studente.getCognome();
            ddn = dateFormat.format(studente.getDataDiNascita());
            numOrdini = Integer.toString(ordineDAO.doRetrieveNumTotOrdiniByEmail(email));
            numLezioniPrenStudente = Integer.toString(lezioneDAO.doRetrieveNumLezioniPrenotateByEmail(email));
            request.setAttribute("numOrdiniStudente", numOrdini);
            request.setAttribute("numLezioniPrenStudente", numLezioniPrenStudente);
        }

        request.setAttribute("email", email);
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("ddn", ddn);

        RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
        dispatcher.forward(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }

}