package controller;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Tutor;
import model.TutorDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ApplyFilter", value = "/apply-filters")
public class ApplyFilters extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            return;
        response.setContentType("application/json");

        ArrayList<Tutor> tutors;

        // Recupero dei filtri inseriti dall'utente dal pannello dei filtri
        String input = request.getParameter("input");
        String days = request.getParameter("days");
        String maxPriceStr = request.getParameter("maxprice");
        String sort = request.getParameter("sort");
        String ratingStr = request.getParameter("rating");
        String offsetStr = request.getParameter("offset");
        TutorDAO tutorDAO = new TutorDAO();

        // Controlli sui parametri ricevuti
        if(input == null || days == null || maxPriceStr == null || sort == null || ratingStr == null || offsetStr == null
        || !days.matches("^(?:[1-7](?:\\|[1-7]){0,6})?$")
        || !maxPriceStr.matches("^\\d+") || Integer.parseInt(maxPriceStr) > 100
        || (!sort.equals("Tutor.tariffa_oraria ASC") && !sort.equals("Tutor.tariffa_oraria DESC") && !sort.equals("Tutor.ore_tutoring DESC") && !sort.equals("Tutor.recensione_media DESC") && !sort.equals(""))
        || !ratingStr.matches("^\\d+") || Integer.parseInt(ratingStr) > 4
        || (Integer.parseInt(offsetStr)%15) != 0){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/errors/error500.jsp");
            dispatcher.forward(request,response);
            return;
        }

        int maxPrice = Integer.parseInt(maxPriceStr);
        int rating = Integer.parseInt(ratingStr);
        int offset = Integer.parseInt(offsetStr);

        if(sort.isEmpty())
            sort = null;
        if(days.isEmpty())
            days = null;

        tutors = tutorDAO.doRetrieveByFilters(input,days,maxPrice,sort,rating,offset);

        tutors.removeIf(Tutor::isBanned);// Rimozione dei tutor bannati dalla lista

        // Conversione della lista di tutor in JSON, utilizzando GSON
        Gson gson = new Gson();
        String tutorsJSON = gson.toJson(tutors);

        // Scrittura nel writer della risposta per passare i dati al Javascript, chiudendolo successivamente
        PrintWriter writer = response.getWriter();
        writer.write(tutorsJSON);
        writer.close();
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
