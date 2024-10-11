package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Studente;
import model.StudenteDAO;
import model.Tutor;
import model.TutorDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

//AL MOMENTO PER LE FOTO LASCIAMO IN SOSPESO
@WebServlet(name = "ChangeName", value = "/change-name")
public class ChangeName extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userEmail = request.getParameter("userEmail");
        String newFirstName = request.getParameter("newFirstName");
        String newLastName = request.getParameter("newLastName");
        String destination;

        //valido l'email, nuovo nome, nuovo cognome, flag admin
        boolean validEmail = true, validFirstName = true, validLastName = true, validAdmin = true, adminFlag = true;
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        Pattern patternName = Pattern.compile("^[^0-9!@#$%^&*()]+$");

        if(newFirstName == null || !patternName.matcher(newFirstName).find() || newFirstName.length() < 1 || newFirstName.length() > 50)
            validFirstName = false;

        if(newLastName == null || !patternName.matcher(newLastName).find() || newLastName.length() < 1 || newLastName.length() > 50)
            validLastName = false;

        if(userEmail == null || !patternEmail.matcher(userEmail).find() || userEmail.length() > 50)
            validEmail = false;


        if(validEmail && validFirstName && validLastName){
            //tutor esposti conservati in 2 liste nella sessione
            ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
            ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");
            TutorDAO tutorDAO = new TutorDAO();
            StudenteDAO studenteDAO = new StudenteDAO();

            Tutor tutorLoggato = (Tutor) request.getSession().getAttribute("tutor");
            Studente studenteLoggato = (Studente) request.getSession().getAttribute("studente");

            //se sono validi, modifico nella sessione e nei bean, nel caso fosse tutor o studente
            if(tutorLoggato != null){
                tutorLoggato.setNome(newFirstName);
                tutorLoggato.setCognome(newLastName);
                tutorDAO.doEditName(tutorLoggato.getEmailUtente(), newFirstName, newLastName);

                if(list1 != null)
                    for(Tutor t : list1)
                        if(t.getEmailUtente().equals(userEmail)){ //si modifica se è presente nella tutor-list
                            t.setNome(newFirstName);
                            t.setCognome(newLastName);
                        }
                if(list2 != null)
                    for(Tutor t : list2)
                        if(t.getEmailUtente().equals(userEmail)){ //si modifica se è presente nella experiencedTutors-list
                            t.setNome(newFirstName);
                            t.setCognome(newLastName);
                        }

                request.setAttribute("tutorEmail", tutorLoggato.getEmailUtente());
            }
            if(studenteLoggato != null){
                studenteLoggato.setNome(newFirstName);
                studenteLoggato.setCognome(newLastName);
                studenteDAO.doEditName(studenteLoggato.getEmailUtente(), newFirstName, newLastName);
            }
            destination = "profile-servlet";
        } else destination = "/WEB-INF/errors/error500.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }
}
