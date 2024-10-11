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

@WebServlet(name = "AdminData", value = "/admin-data")
public class AdminData extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Lista di tutti i tutor e di tutti gli studenti
        StudenteDAO studenteDAO = new StudenteDAO();
        TutorDAO tutorDAO = new TutorDAO();
        ArrayList<Studente> studenti = new ArrayList<>();
        ArrayList<Tutor> tutors = new ArrayList<>();
        ArrayList<Studente> studentiBannati = new ArrayList<>();
        ArrayList<Tutor> tutorsBannati =  new ArrayList<>();

        //Guadagno totale del sito, num di ore di tutoring totali e recensione media
        double guadagnoTot = 0;
        int numOreTutoringTot = 0;
        double recensMediaTot = 0;
        for(Tutor t : tutorDAO.doRetrieveAll()) {
            guadagnoTot += t.getGuadagno();
            numOreTutoringTot += t.getOreTutoring();
            recensMediaTot += t.getNumRecensioni();
            if(t.isBanned())
                tutorsBannati.add(t);
            else tutors.add(t);
        }
        for(Studente s : studenteDAO.doRetrieveAll())
            if(s.isBanned())
                studentiBannati.add(s);
            else studenti.add(s);

        recensMediaTot /= tutorDAO.doRetrieveAll().size();

        //Numero di ordini e di lezioni prenotate (quelle di carrello + quelle ordinate) in totale
        OrdineDAO ordineDAO = new OrdineDAO();
        int numOrdiniTotali = ordineDAO.doRetrieveNumTotOrdini();
        LezioneDAO lezioneDAO = new LezioneDAO();
        int numLezioniTotali = lezioneDAO.doRetrieveNumLezioniPrenotate();

        TicketDAO ticketDAO = new TicketDAO();
        ArrayList<Ticket> tickets = ticketDAO.doRetrieveAll();

        request.setAttribute("studenti", studenti);
        request.setAttribute("tutors", tutors);
        request.setAttribute("studentiBannati", studentiBannati);
        request.setAttribute("tutorsBannati", tutorsBannati);
        request.setAttribute("guadagnoTot", guadagnoTot);
        request.setAttribute("numOreTutoringTot", numOreTutoringTot);
        request.setAttribute("recensMediaTot", recensMediaTot);
        request.setAttribute("numOrdiniTotali", numOrdiniTotali);
        request.setAttribute("numLezioniTotali", numLezioniTotali);
        request.setAttribute("numTutorTotali", tutors.size() + tutorsBannati.size());
        request.setAttribute("numStudentiTotali", studenti.size() + studentiBannati.size());
        request.setAttribute("ticketList", tickets);
        ArrayList<String> dates = new ArrayList<>();
        for(Ticket t : tickets){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dates.add(dateFormat.format(t.getDataInvio()));
        }
        request.setAttribute("dateTickets", dates);


        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin-data.jsp");
        dispatcher.forward(request, response);
         //TODO: visualizziamo anche i ticket nella jsp, prendi pure quelli
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
