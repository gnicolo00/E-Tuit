package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Studente;
import model.Tutor;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout-servlet")
public class LogoutServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Studente studente = (Studente) session.getAttribute("studente");
        Tutor tutor = (Tutor) session.getAttribute("tutor");
        Studente admin = (Studente) session.getAttribute("admin");

        if(studente != null) {
            session.removeAttribute("studente");
            session.removeAttribute("cart");
        }
        else if(tutor != null) {
            session.removeAttribute("tutor");
        }
        else if(admin != null) {
            session.removeAttribute("admin");
        }

        response.sendRedirect("home-servlet");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
