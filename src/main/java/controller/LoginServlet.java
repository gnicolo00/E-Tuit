package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

@WebServlet(name = "LoginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Recupero dell'email e della password inseriti dall'utente
        String email = request.getParameter("email-login");
        String password = request.getParameter("password-login");
        System.out.println("email-login: " + email);

        //Validazione dei dati lato server
        boolean emailValid = true;
        boolean passwordValid = true;
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{10,}$");
        //email
        if(email == null || !patternEmail.matcher(email).find() || email.length() > 50)
            emailValid = false;
        //password
        if(password == null || !patternPassword.matcher(password).find() || password.length() > 20)
            passwordValid = false;


        //Se i dati sono validi, posso procedere con il verificare se i dati sono corretti per accedere
        if(emailValid && passwordValid){
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(60*30);

            StudenteDAO studenteDAO = new StudenteDAO();
            TutorDAO tutorDAO = new TutorDAO();
            Studente studente = studenteDAO.doRetrieveByEmailAndPassword(email,password);
            Tutor tutor = tutorDAO.doRetrieveByEmailAndPassword(email,password);
            Studente admin = studenteDAO.doRetrieveAdminByEmailAndPassword(email,password);

            if(studente != null && !studente.isBanned()) {
                session.setAttribute("studente",studente);

                //Ripristino del carrello dell'utente loggato e quello dell'utente non autenticato
                CarrelloDAO carrelloDAO = new CarrelloDAO();
                Carrello carrelloAuth = carrelloDAO.doRetrieveByOwner(email);
                Carrello carrelloUnauth = (Carrello) request.getSession().getAttribute("cart");

                //Modifico il bean inserendo email e carico aggiornato
                carrelloUnauth.setEmailProprietario(carrelloAuth.getEmailProprietario());
                carrelloUnauth.setCarico(carrelloUnauth.getCarico() + carrelloAuth.getCarico());

                //Aggiorno il carico del carrello dello studente salvato nel database
                carrelloDAO.doUpdateCarico(carrelloUnauth);

                //La lista delle lezione prenotate dall'utente non autenticato va leggermente modificata
                ArrayList<Lezione> unauthenticatedUserLezioni = carrelloUnauth.getListaLezioni();
                for(Lezione lezione : unauthenticatedUserLezioni){
                    lezione.setEmailAcquirente(email);//Setto l'email (che prima era fittizia, stringa vuota) di chi la compra, cioè lo studente loggato
                    carrelloDAO.doAddLezione(lezione);//Aggiungo la lezione al carrello dello studente, nel database,
                }

                //Qui aggiorno la lista del bean contenuto nella sessione
                ArrayList<Lezione> newList = new ArrayList<>(); //creo una nuova lista
                newList.addAll(carrelloAuth.getListaLezioni()); //alla nuova lista concateno quella del carrello dell'utente
                newList.addAll(unauthenticatedUserLezioni); //alla nuova lista concateno quella delle lezioni prima prenotate dall'utente non autenticato, poi aggiornata
                carrelloUnauth.setListaLezioni(newList); //la imposto nel bean
            }
            else if((studente != null && studente.isBanned() || (tutor != null && tutor.isBanned()))){// se l'utente non esiste, non esisterà né l'attributo "tutor" né l'attributo "studente". In tal caso, gestiremo l'errore di login
                session.setAttribute("login-error","Sei stato bannato dal sito.");
            }
            else if(tutor != null && !tutor.isBanned()) {
                session.setAttribute("tutor",tutor);
            }
            else if (admin != null) {
                session.setAttribute("admin",admin);
            }
            else {// se l'utente non esiste, non esisterà né l'attributo "tutor" né l'attributo "studente". In tal caso, gestiremo l'errore di login
                session.setAttribute("login-error","Email o password errati. Riprova.");
            }

            response.sendRedirect("home-servlet");

        } else response.sendRedirect("login-error.jsp");
    }
}
