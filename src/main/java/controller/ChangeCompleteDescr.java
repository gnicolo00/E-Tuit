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

@WebServlet(name = "ChangeCompleteDescr", value = "/change-complete-descr")
public class ChangeCompleteDescr extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        String newCompleteDescription = request.getParameter("newCompleteDescription");
        String destination;

        //tutor esposti conservati in 2 liste nella sessione
        ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
        ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");

        //tutor dalla sessione
        Tutor tutorLoggato = (Tutor) request.getSession().getAttribute("tutor");
        String userEmail = tutorLoggato.getEmailUtente();

        if (newCompleteDescription != null && !newCompleteDescription.isEmpty() && newCompleteDescription.length() <= 1500) {
            //cerco il tutor di cui va cambiato qualcosa in entrambe le liste e modifico
            if(list1 != null)
                for(Tutor t : list1)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setDescrizioneCompleta(newCompleteDescription);
            if(list2 != null)
                for(Tutor t : list2)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setDescrizioneCompleta(newCompleteDescription);

            tutorDAO.doEditCompleteDescription(userEmail, newCompleteDescription);
            tutorLoggato.setDescrizioneCompleta(newCompleteDescription);
            destination = "profile-servlet";
        } else destination = "WEB-INF/errors/error500.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);

        //TODO: nell'admin metti descrizioni di default così in questa servlet le validi a prescindere e amen
    }
}

