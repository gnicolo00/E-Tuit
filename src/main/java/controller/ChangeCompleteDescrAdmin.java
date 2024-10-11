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

@WebServlet(name = "ChangeCompleteDescriptionAdmin", value = "/change-compl-descr-admin")
public class ChangeCompleteDescrAdmin extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        String destination;
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        String userEmail = request.getParameter("changeComplDescrEmail");
        boolean validEmail = true;
        String newCompleteDescription = request.getParameter("newCompleteAdminDescription");

        //tutor esposti conservati in 2 liste nella sessione
        ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
        ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");

        if(userEmail == null)
            validEmail = false;
        else if(userEmail.isEmpty() || !patternEmail.matcher(userEmail).find() || userEmail.length() > 50)
            validEmail = false;

        if (validEmail) {
            //cerco il tutor di cui va cambiato qualcosa in entrambe le liste e modifico
            if(list1 != null)
                for (Tutor t : list1)
                    if (t.getEmailUtente().equals(userEmail))
                        t.setDescrizioneCompleta(newCompleteDescription);
            if (list2 != null)
                for (Tutor t : list2)
                    if (t.getEmailUtente().equals(userEmail))
                        t.setDescrizioneCompleta(newCompleteDescription);
            tutorDAO.doEditCompleteDescription(userEmail, newCompleteDescription);
            destination = "admin-data";
        } else destination = "WEB-INF/errors/error500.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }
}
