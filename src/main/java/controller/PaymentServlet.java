package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "PaymentServlet", value = "/payment-servlet")
public class PaymentServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Se l'utente ha effettuato il login, si procede con il pagamento.
        if(request.getSession().getAttribute("studente") != null){
            //Estrazione del carrello dalla sessione
            Carrello carrello = (Carrello) request.getSession().getAttribute("cart");
            //Creo un ordine
            Ordine ordine = new Ordine();

            TutorDAO tutorDAO = new TutorDAO();

            Date date = new Date();
            ordine.setDataOrdine(date);
            //Set del prezzo totale, numero di lezioni contenute e l'email di chi effettua l'acquisto.
            ordine.setEmailAcquirente(carrello.getEmailProprietario());
            //Salvataggio dell'ordine nel database, essenziale per poter ricavare il suo ID.
            OrdineDAO ordineDAO = new OrdineDAO();
            ordineDAO.doSave(ordine);
            //Conversione di tutte le lezioni in lezioni prenotate
            for(Lezione lezione : carrello.getListaLezioni()){
                lezione.setPrenotata(true);
                lezione.setIDOrdine(ordine.getID());
                ordineDAO.doSaveLezione(lezione); //la lezione prenotata viene salvata nel datatabase
                tutorDAO.doSumEarnings(lezione.getEmailTutor(), lezione.getPrezzo());
            }
            ordine.setLezioniPrenotate(carrello.getListaLezioni());

            // svuotamento del carrello
            carrello.setCarico(0);
            CarrelloDAO carrelloDAO = new CarrelloDAO();
            carrelloDAO.doUpdateCarico(carrello);// svuotamento del DAO
            carrello.svuota();// svuotamento del bean

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/thank-you.jsp");
            dispatcher.forward(request,response);
        }
        //altrimenti, se l'utente non è autenticato...
        else {
            request.getSession().setAttribute("unauthenticated-user-error", "Esegui il login per poter effettuare l'ordine.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request,response);
        }

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doGet(request, response);
    }
}
