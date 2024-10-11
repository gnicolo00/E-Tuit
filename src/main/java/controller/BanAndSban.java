package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

@WebServlet(name = "BanAndSban", value = "/ban-and-sban")
public class BanAndSban extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        String emailUtente = request.getParameter("emailUtenteBan");
        String destination;

        //Lista di tutti i tutor e di tutti gli studenti
        StudenteDAO studenteDAO = new StudenteDAO();
        TutorDAO tutorDAO = new TutorDAO();
        Studente studente = null;
        Tutor tutor = null;

        ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");
        ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");

        if(emailUtente != null && !emailUtente.isEmpty() && patternEmail.matcher(emailUtente).find() && emailUtente.length() <= 50) {
            destination = "admin-data";

            tutor = tutorDAO.doRetrieveByEmail(emailUtente);
            studente = studenteDAO.doRetrieveByEmail(emailUtente);

            if(tutor != null){ //nel caso del tutor
                //cerco il tutor di cui va cambiato lo stato del bean in entrambe le liste

                if(list1 != null)
                    for (Tutor t : list1)
                        if (t.getEmailUtente().equals(tutor.getEmailUtente()))
                            t.setBanned(!t.isBanned());

                if(list2 != null)
                    for (Tutor t : list2)
                        if (t.getEmailUtente().equals(tutor.getEmailUtente()))
                            t.setBanned(!t.isBanned());

                tutor.setBanned(!tutor.isBanned());
                tutorDAO.doUpdateBan(tutor.isBanned(), emailUtente);
            }
            if(studente != null) {//nel caso dello studente
                studente.setBanned(!studente.isBanned());
                studenteDAO.doUpdateBan(studente.isBanned(), emailUtente);
            }
        }
        else {
            destination = "/WEB-INF/errors/error500.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
