package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Ordine;
import model.OrdineDAO;
import model.Studente;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

@WebServlet(name = "OrdersServlet", value = "/orders-servlet")
public class OrdersServlet extends HttpServlet {
    /**
     * In questo metodo vengono estrapolati tutti gli ordini dal database relativi ad uno specifico utente,
     * inserendo una lista di questi nella sessione. L'email è passata come parametro "hidden" ed estrapolato dalla request.
     * @param request La richiesta Http.
     * @param response La risposta Http.
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Studente studente = (Studente) request.getSession().getAttribute("studente");
        String email = studente.getEmailUtente();

        OrdineDAO ordineDAO = new OrdineDAO();
        ArrayList<Ordine> orders = ordineDAO.doRetrieveByEmailStudente(email);
        Collections.reverse(orders);

        request.getSession().setAttribute("orders",orders);

        //ArrayList che contiene le date degli ordini nel formato giusto visualizzabili nello storico ordini
        ArrayList<String> dates = new ArrayList<>();
        for(Ordine ordine : orders){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dates.add(dateFormat.format(ordine.getDataOrdine()));
        }
        request.setAttribute("dateVisualizzabili", dates);

        RequestDispatcher dispatcher = request.getRequestDispatcher("orders.jsp");
        dispatcher.forward(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
