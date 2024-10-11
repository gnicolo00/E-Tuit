package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Tutor;
import model.TutorDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

@WebServlet(name = "TutorPage", value = "/tutor-page")
public class TutorPage extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        String destination;
        String tutorEmail = request.getParameter("tutorEmail"); //parametro inviato da un form presente in una jsp
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");

        if(tutorEmail == null || !patternEmail.matcher(tutorEmail).find() || tutorEmail.length() > 50) {
            destination = "/WEB-INF/errors/error500.jsp";
        } else {
            Tutor tutor = tutorDAO.doRetrieveByEmail(tutorEmail);
            destination = "tutor-page.jsp";
            request.setAttribute("tutor-tutorPage", tutor);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request,response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}