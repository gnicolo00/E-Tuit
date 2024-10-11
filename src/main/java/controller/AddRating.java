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

@WebServlet(name = "AddRating", value = "/add-rating")
public class AddRating extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        double ratingParam = Double.parseDouble(request.getParameter("rating"));

        String tutorEmail = request.getParameter("tutorEmail");
        ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
        ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");
        Tutor tutorList1 = null, tutorList2 = null;

        //cerco il tutor di cui va cambiato qualcosa in entrambe le liste
        if(list1 != null)
            for(Tutor t : list1)
                if(t.getEmailUtente().equals(tutorEmail))
                    tutorList1 = t;
        if(list2 != null)
            for(Tutor t : list2)
                if(t.getEmailUtente().equals(tutorEmail))
                    tutorList2 = t;

        TutorDAO tutorDAO = new TutorDAO();

        if(ratingParam >= 0 && ratingParam <= 5){
            Tutor tutor = tutorDAO.doRetrieveByEmail(tutorEmail);
            double nuovaMedia = (tutor.getRecensioneMedia()* tutor.getNumRecensioni() + ratingParam)/(tutor.getNumRecensioni() + 1);
            double nuovaMediaTroncata = Math.round(nuovaMedia * 10.0) / 10.0;
            int nuovoNumRec = tutor.getNumRecensioni() + 1;
            tutor.setRecensioneMedia(nuovaMediaTroncata);
            tutor.setNumRecensioni(nuovoNumRec);

            tutorDAO.doEditRating(tutorEmail, nuovaMediaTroncata, tutor.getNumRecensioni());
            if(tutorList1 != null){
                tutorList1.setRecensioneMedia(nuovaMediaTroncata);
                tutorList1.setNumRecensioni(nuovoNumRec);
            }
            if(tutorList2 != null){
                tutorList2.setRecensioneMedia(nuovaMediaTroncata);
                tutorList2.setNumRecensioni(nuovoNumRec);
            }
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("tutor-page");
        requestDispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
