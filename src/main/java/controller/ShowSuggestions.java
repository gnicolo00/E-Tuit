package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TutorDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ShowSuggestions", value = "/show-suggestions")
public class ShowSuggestions extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            return;
        response.setContentType("application/json");

        // Recupero dell'input da tastiera inserito dall'utente nella barra di ricerca nella Home
        String input = request.getParameter("input");
        TutorDAO tutorDAO = new TutorDAO();
        ArrayList<String> suggestions = tutorDAO.doSuggest(input);

        // Conversione della lista di suggerimenti in JSON, utilizzando GSON
        Gson gson = new Gson();
        String suggestionsJSON = gson.toJson(suggestions);

        // Scrittura nel writer della risposta per passare i dati al Javascript, chiudendolo successivamente
        PrintWriter writer = response.getWriter();
        writer.write(suggestionsJSON);
        writer.close();
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
