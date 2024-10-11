package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Tutor;
import model.TutorDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

@WebServlet(name = "ChangeHoursRate", value = "/change-hours-rate")
public class ChangeHoursRate extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        String tariffaOrariaString = request.getParameter("newTariffaOraria");
        String destination;
        double hoursRate = 0;
        Tutor tutor = (Tutor) request.getSession().getAttribute("tutor");
        String userEmail = tutor.getEmailUtente();
        boolean validHRate = true;

        //tariffa oraria
        if(tariffaOrariaString == null)
            validHRate = false;
        else {
            try {
                hoursRate =  Double.parseDouble(tariffaOrariaString);
            } catch (NumberFormatException e){
                validHRate = false;
            }
            if(hoursRate < 1 || hoursRate > 100)
                validHRate = false;
        }

        if(validHRate){
            tutorDAO.doEditHoursRate(userEmail, hoursRate); //cambio nel db
            //modifico i tutor esposti conservati in 2 liste nella sessione
            ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
            ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");

            //cerco il tutor in questione
            if(list1 != null)
                for(Tutor t : list1)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setTariffaOraria(hoursRate);
            if(list2 != null)
                for(Tutor t : list2)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setTariffaOraria(hoursRate);

            destination = "profile-servlet";

            //modifico il bean del tutor nella sessione
            Tutor sessionTutor = (Tutor) request.getSession().getAttribute("tutor");
            if(sessionTutor != null)
                sessionTutor.setTariffaOraria(hoursRate);
        } else destination = "/web-INF/errors/error500.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }
}