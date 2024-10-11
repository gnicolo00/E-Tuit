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
import java.util.regex.Pattern;

@WebServlet(name = "BookLesson", value = "/book-lesson-servlet")
public class BookLessonServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        HttpSession session = request.getSession();

        //se non c'è alcun tutor o admin loggato, c'è l'utente non autenticato oppure studente loggato
        if(session.getAttribute("tutor") == null && session.getAttribute("admin") == null){
            //Salvo l'email del tutor, che è conservata nella sessione (settata da TutorPage servlet) e rimuovo l'attributo
            String tutorEmail = request.getParameter("tutorEmail");
            Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
            //Salvo il numero di ore inserite per prenotare la lezione e la materia d'interesse.
            boolean validNumOrePrenotate = true, validSettore = true, validEmail = true;
            int numOrePrenotate = 0;
            String numOrePrenString = request.getParameter("numOrePrenotate");
            String materia = request.getParameter("materiaLezione");

            //validazione lato server dei dati inseriti
            if(numOrePrenString == null)
                validNumOrePrenotate = false;
            else numOrePrenotate = Integer.parseInt(numOrePrenString);

            if(materia == null || materia.isEmpty())
                validSettore = false;

            if(numOrePrenotate < 1)
                validNumOrePrenotate = false;

            if(tutorEmail == null || tutorEmail.isEmpty() || !patternEmail.matcher(tutorEmail).find() || tutorEmail.length() > 50)
                validEmail = false;

                //se sono corretti, procedo con l'aggiunta al carrello
            if(validNumOrePrenotate && validSettore && validEmail) {//Creo il bean della lezione che si vuole prenotare.
                Tutor tutor = tutorDAO.doRetrieveByEmail(tutorEmail);

                //Estraggo il carrello dalla sessione
                Carrello carrello = (Carrello) session.getAttribute("cart");

                //Salvo l'email dello studente loggato che vuole prenotare la lezione
                Studente studente = (Studente) session.getAttribute("studente");

                Lezione lezione = new Lezione();
                lezione.setID(carrello.getCarico() + 1); //ID fittizio, è questo l'ID per le lezioni prenotate da un utente non autenticato
                lezione.setMateria(materia);
                lezione.setOre(numOrePrenotate);
                lezione.setPrezzo(numOrePrenotate * tutor.getTariffaOraria());
                lezione.setEmailTutor(tutor.getEmailUtente());
                lezione.setNomeTutor(tutor.getNome() + " " + tutor.getCognome());
                lezione.setPrenotata(false);
                lezione.setEmailAcquirente(carrello.getEmailProprietario());

                //Aggiungo la lezione al bean del carrello
                carrello.aggiungiLezione(lezione);

                if (studente != null) {//se l'utente loggato è uno studente
                    CarrelloDAO carrelloDAO = new CarrelloDAO();
                    carrelloDAO.doAddLezione(lezione);//aggiungo la lezione al carrello nel database
                    carrelloDAO.doUpdateCarico(carrello);
                }//se l'utente non è autenticato, uso l'email usata nel carrello che sarebbe una stringa vuota, è fittizia.
            }

            //Indirizzo alla pagina di conferma di aggiunta al carrello
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/book-lesson.jsp"); //da vedere se la trova la jsp, da controllare
            dispatcher.forward(request, response);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
//TODO: VALIDAZIONE LATO SERVER DEL FORM DELLA LEZIONE
//TODO: gestione errori nelle varie servlet
