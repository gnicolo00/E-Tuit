package controller;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "CompleteRegistration", value = "/complete-registration")
public class CompleteRegistration extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //guadagno all'inizio è 0
        //ore tutoring alla creazione del profilo è 0
        //recensione media all'inizio è 0
        //isAmbasciatore all'inizio è false

        HttpSession session = request.getSession();
        Tutor tutor = (Tutor) session.getAttribute("tutor");
        String[] titoli = request.getParameterValues("titolo");
        String[] settori = request.getParameterValues("settore");
        String tariffaOrariaString = request.getParameter("tariffaOraria");
        String[] giorni = request.getParameterValues("giornoDisponibile");
        String descrizioneCard = request.getParameter("descrizione-card");
        String descrizioneCompleta = request.getParameter("descrizione-completa");

        //Validazione lato server dei dati inseriti
        int lung = 0, i;
        boolean titoliValidi = true, settoriValidi = true, descrCardValida = true, descrCompletaValida = true, tariffaOrariaValida = true, giorniValidi = true;
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s']+$");
        ArrayList<Boolean> findMatchTitoli = new ArrayList<>();
        ArrayList<Boolean> findMatchSettori = new ArrayList<>();

        //titoli e settori
        if(titoli == null)
            titoliValidi = false;
        else {
            for(i = 0; i < titoli.length; i++) {
                lung += titoli[i].length();
                Matcher matcher = pattern.matcher(titoli[i]);
                findMatchTitoli.add(matcher.find());
            }
            if(lung > 500 || findMatchTitoli.contains(false))
                titoliValidi = false;
        }

        lung = 0;

        if(settori == null)
            settoriValidi = false;
        else {
            for(i = 0; i < settori.length; i++) {
                lung += settori[i].length();
                Matcher matcher = pattern.matcher(settori[i]);
                findMatchSettori.add(matcher.find());
            }
            if(lung > 150 || findMatchSettori.contains(false))
                settoriValidi = false;
        }

        //giorni di disponibilità
        if(giorni == null)
            giorniValidi = false;
        else {
            //itero sulla lista dei giorni, per ogni elemento controllo se è un martedì etc e se cè almeno uno, si mette false e break
            for(String giorno : giorni){
                if(!giorno.equals("lunedì")
                   && !giorno.equals("martedì")
                   && !giorno.equals("mercoledì")
                   && !giorno.equals("giovedì")
                   && !giorno.equals("venerdì")
                   && !giorno.equals("sabato")
                   && !giorno.equals("domenica")){
                    giorniValidi = false;
                    break;
                }
            }
        }

        //tariffa oraria
        double tariffaOraria = 0;
        if(tariffaOrariaString == null)
            tariffaOrariaValida = false;
        else {
            try {
                tariffaOraria =  Double.parseDouble(tariffaOrariaString);
            } catch (NumberFormatException e){
                tariffaOrariaValida = false;
            }
            if(tariffaOraria < 1 || tariffaOraria > 100)
                tariffaOrariaValida = false;
        }

        //descrizione per la card e quella completa
        if(descrizioneCard == null || descrizioneCard.length() > 150)
            descrCardValida = false;

        if(descrizioneCompleta == null || descrizioneCompleta.length() > 1500)
            descrCompletaValida = false;

        if(tariffaOrariaValida && titoliValidi && settoriValidi && giorniValidi && descrCardValida && descrCompletaValida){
            TutorDAO tutorDAO = new TutorDAO();
            tutor.setTitoliFromArray(titoli);
            tutor.setSettoriFromArray(settori);
            tutor.setGuadagno(0);
            tutor.setOreTutoring(0);
            tutor.setTariffaOraria(tariffaOraria);
            tutor.setGiorniFromArray(giorni);
            tutor.setDescrizioneCard(descrizioneCard);
            tutor.setDescrizioneCompleta(descrizioneCompleta);
            tutor.setNumRecensioni(0);
            tutor.setRecensioneMedia(0);
            tutor.setAmbasciatore(false);
            tutorDAO.doSave(tutor);

            response.sendRedirect("home-servlet");
        } else response.sendRedirect("error-compl-reg.jsp");
    }
}
