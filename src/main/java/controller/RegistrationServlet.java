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
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@WebServlet(name = "RegistrationServlet", value = "/registration-servlet")
public class RegistrationServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Ottengo le informazioni inserite dall'utente per registrarsi
        String type = request.getParameter("type"); //questo campo permetterà di distinguere il profilo tutor da quello studente
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String gender = request.getParameter("gender");
        String birthdayString = request.getParameter("birthday");
        String email = request.getParameter("email-registration");
        String password = request.getParameter("password-registration");

        //Conversione della data
        String[] date = birthdayString.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);  //i mesi vanno da 0 a 11
        calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
        java.util.Date birthday = calendar.getTime();

        //Validazione dei dati lato server
        boolean firstNameValid = true;
        boolean lastNameValid = true;
        boolean genderValid = true;
        boolean typeValid = true;
        boolean dateValid = true;
        boolean emailValid = true;
        boolean passwordValid = true;
        Pattern patternName = Pattern.compile("^[^0-9!@#$%^&*()]+$");
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        Pattern patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{10,}$");
        //nome
        if(firstName == null || !patternName.matcher(firstName).find() || firstName.length() < 1 || firstName.length() > 50)
            firstNameValid = false;
        //cognome
        if(lastName == null || !patternName.matcher(lastName).find() || lastName.length() < 1 || lastName.length() > 50)
            lastNameValid = false;
        //genere
        if(gender == null)
            genderValid = false;
        //tipo di account (tutor o studente)
        if(type == null)
            typeValid = false;
        //data di nascita
        Date today = new Date();
        if(today.before(birthday))
            dateValid = false;
        //email
        if(email == null || !patternEmail.matcher(email).find() || email.length() > 50)
            emailValid = false;
        //password
        if(password == null || !patternPassword.matcher(password).find() || password.length() > 20)
            passwordValid = false;

        //se tutti i dati sono corretti, posso proseguire con la creazione dell'account
        if(firstNameValid && lastNameValid && genderValid && typeValid && dateValid && emailValid && passwordValid){
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(60*30);

            //controllo se l'email appartiene ad un profilo già esistente
            StudenteDAO studenteDAO = new StudenteDAO();
            TutorDAO tutorDAO = new TutorDAO();
            Studente studenteByEmail = studenteDAO.doRetrieveByEmail(email);
            Tutor tutorByEmail = tutorDAO.doRetrieveByEmail(email);

            if(studenteByEmail != null || tutorByEmail != null){// blocco la registrazione, non è possibile perchè esiste già un utente con questa password
                session.setAttribute("registration-error","L'email inserita appartiene ad un account già esistente.");
                response.sendRedirect("home-servlet");
            }
            else {
                if(type.equals("student")){
                    Studente studente = new Studente();
                    studente.setNome(firstName);
                    studente.setCognome(lastName);
                    studente.setSesso(gender);
                    studente.setDataDiNascita(birthday);
                    studente.setEmailUtente(email);
                    studente.setPassword(password);
                    studente.setAdmin(false);
                    studente.setBanned(false);
                    studenteDAO.doSave(studente);
                    // Creazione del carrello dell'utente registrato, facendo attenzione a ripristinare anche quello dell'utente non autenticato.
                    CarrelloDAO carrelloDAO = new CarrelloDAO();
                    Carrello carrello = (Carrello) request.getSession().getAttribute("cart");
                    carrello.setEmailProprietario(email);
                    carrelloDAO.doSave(carrello);
                    for(Lezione lezione : carrello.getListaLezioni()){
                        lezione.setEmailAcquirente(email);
                        carrelloDAO.doAddLezione(lezione);
                    }
                    session.setAttribute("studente",studente);
                    response.sendRedirect("home-servlet");
                    //inserimento foto
                    //posso procedere a fare l'accesso automatico con l'account appena registrato
                }
                else if(type.equals("tutor")){
                    Tutor tutor = new Tutor();
                    tutor.setNome(firstName);
                    tutor.setCognome(lastName);
                    tutor.setSesso(gender);
                    tutor.setDataDiNascita(birthday);
                    tutor.setEmailUtente(email);
                    tutor.setPassword(password);
                    tutor.setAdmin(false);
                    tutor.setBanned(false);
                    session.setAttribute("tutor", tutor);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/header/complete-registration.jsp");
                    dispatcher.forward(request, response);
                    //inserimento foto
                }
            }
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("error-reg.jsp"); //temporaneo
            dispatcher.forward(request, response);
        }
    }
}