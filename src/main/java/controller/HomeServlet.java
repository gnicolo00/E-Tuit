package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Carrello;
import model.Tutor;
import model.TutorDAO;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "HomeServlet", value = "/home-servlet")
public class HomeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /*
        Recupero dei quattro tutor con maggiore esperienza (numero di ore tutoring totali più alto)
        da visualizzare nella Home.
        */
        TutorDAO tutorDAO = new TutorDAO();
        ArrayList<Tutor> experiencedTutors = tutorDAO.doRetrieveByHighestHours();
        request.getSession().setAttribute("experiencedTutors",experiencedTutors);

        //Creazione del carrello per l'utente non (ancora) autenticato e lo inserisco nella sessione.
        if(request.getSession().getAttribute("cart") == null){
            Carrello carrello = new Carrello();
            carrello.setCarico(0);
            carrello.setListaLezioni(new ArrayList<>());
            carrello.setEmailProprietario("");
            request.getSession().setAttribute("cart", carrello);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request,response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
