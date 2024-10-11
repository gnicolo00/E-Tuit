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

@WebServlet(name = "ShowTutors", value = "/show-tutors")
public class ShowTutors extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String input = request.getParameter("search-bar");
        TutorDAO tutorDAO = new TutorDAO();
        ArrayList<Tutor> tutors;

        if (input == null || input.isBlank())
            tutors = tutorDAO.doRetrieveBySearchInput("");
        else
            tutors = tutorDAO.doRetrieveBySearchInput(input);

        tutors.removeIf(Tutor::isBanned);// Rimozione dei tutor bannati dalla lista

        request.getSession().setAttribute("tutor-list",tutors);
        request.setAttribute("given-input",input);

        RequestDispatcher dispatcher = request.getRequestDispatcher("tutors-grid.jsp");
        dispatcher.forward(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
