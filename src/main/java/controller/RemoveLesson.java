package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;

@WebServlet(name = "RemoveLesson", value = "/remove-lesson")
public class RemoveLesson extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Se il tipo di richiesta non è AJAX, la servlet non farà nulla.
        if(!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            return;

        //Ottengo l'id della lezione da rimuovere dal carrello.
        int id = Integer.parseInt(request.getParameter("id"));

        //Modifica nel bean e nel database.
        Carrello carrello = (Carrello) request.getSession().getAttribute("cart");
        carrello.rimuoviLezione(id);
        if (request.getSession().getAttribute("studente") != null) {// se il carrello in questione è di un utente autenticato, interagiamo anche con il database
            LezioneDAO lezioneDAO = new LezioneDAO();
            CarrelloDAO carrelloDAO = new CarrelloDAO();
            Lezione lezione = lezioneDAO.doRetrieveByID(id);
            carrelloDAO.doRemoveLezione(lezione);
            carrelloDAO.doUpdateCarico(carrello);// decrementa il carico del carrello di un'unità dopo la rimozione della lezione.
        }

        //set content type della response, dove metto le stringhe json
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
