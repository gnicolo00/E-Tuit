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
import java.util.Arrays;
import java.util.regex.Pattern;

@WebServlet(name = "ChangeDays", value = "/change-days")
public class ChangeDays extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TutorDAO tutorDAO = new TutorDAO();
        String[] giorni = request.getParameterValues("nuovoGiornoDisponibile");
        Tutor tutor = (Tutor) request.getSession().getAttribute("tutor");
        String userEmail = tutor.getEmailUtente();
        boolean validDays = true;

        if(giorni == null)
            validDays = false;
        else {
            ArrayList<String> listaGiorni = new ArrayList<>(Arrays.asList(giorni));
            if (!listaGiorni.contains("lunedì") &&
                !listaGiorni.contains("martedì") &&
                !listaGiorni.contains("mercoledì") &&
                !listaGiorni.contains("giovedì") &&
                !listaGiorni.contains("venerdì") &&
                !listaGiorni.contains("sabato") &&
               !listaGiorni.contains("domenica")
            ) validDays = false;
        }

        if(validDays){
            String giorniNuovi = ""; //ottengo la stringa con i giorni da memorizzare nel database
            for(String giorno : giorni)
                switch (giorno){
                    case "lunedì": giorniNuovi = giorniNuovi.concat("1"); break;
                    case "martedì": giorniNuovi = giorniNuovi.concat("2"); break;
                    case "mercoledì": giorniNuovi = giorniNuovi.concat("3"); break;
                    case "giovedì": giorniNuovi = giorniNuovi.concat("4"); break;
                    case "venerdì": giorniNuovi = giorniNuovi.concat("5"); break;
                    case "sabato": giorniNuovi = giorniNuovi.concat("6"); break;
                    case "domenica": giorniNuovi = giorniNuovi.concat("7"); break;
                }
            //cambio nel db
            tutorDAO.doEditDays(userEmail, giorniNuovi);

            //modifico i tutor esposti conservati in 2 liste nella sessione
            ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
            ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");

            //cerco il tutor in questione
            if(list1 != null)
                for(Tutor t : list1)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setGiorni(giorniNuovi);
            if(list2 != null)
                for(Tutor t : list2)
                    if(t.getEmailUtente().equals(userEmail))
                        t.setGiorni(giorniNuovi);

            //modifico il bean del tutor nella sessione
            Tutor sessionTutor = (Tutor) request.getSession().getAttribute("tutor");
            if(sessionTutor != null)
                sessionTutor.setGiorni(giorniNuovi);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("profile-servlet");
        dispatcher.forward(request, response);
    }
}