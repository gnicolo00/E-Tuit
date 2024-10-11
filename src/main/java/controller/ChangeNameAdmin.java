package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.StudenteDAO;
import model.Tutor;
import model.TutorDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

@WebServlet(name = "ChangeNameAdmin", value = "/change-name-admin")
public class ChangeNameAdmin extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Pattern patternEmail = Pattern.compile("^[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,3}$");
        Pattern patternName = Pattern.compile("^[^0-9!@#$%^&*()]+$");
        boolean validFirstName = true, validLastName = true;
        String userEmail = null, newFirstName = null, newLastName = null, destination;
        String studentEmail = request.getParameter("changeNameStudentEmail");
        String tutorEmail = request.getParameter("changeNameTutorEmail");
        boolean validEmail = true, studentBoo = false, tutorBoo = false;

        if(studentEmail != null) {
            studentBoo = true;
            userEmail = studentEmail;
        }
        else if(tutorEmail != null) {
            tutorBoo = true;
            userEmail = tutorEmail;
        }
        else validEmail = false;

        if(studentBoo){
            newFirstName = request.getParameter("newFirstNameStudent");
            newLastName = request.getParameter("newLastNameStudent");
        }
        if(tutorBoo){
            newFirstName = request.getParameter("newFirstNameTutor");
            newLastName = request.getParameter("newLastNameTutor");
        }

        if(newFirstName == null || !patternName.matcher(newFirstName).find() || newFirstName.length() < 1 || newFirstName.length() > 50)
            validFirstName = false;

        if(newLastName == null || !patternName.matcher(newLastName).find() || newLastName.length() < 1 || newLastName.length() > 50)
            validLastName = false;

        if(userEmail == null || !patternEmail.matcher(userEmail).find() || userEmail.length() > 50)
            validEmail = false;

        if(validEmail && validFirstName && validLastName){
            //tutor esposti conservati in 2 liste nella sessione
            ArrayList<Tutor> list1 = (ArrayList<Tutor>) request.getSession().getAttribute("tutor-list");
            ArrayList<Tutor> list2 = (ArrayList<Tutor>) request.getSession().getAttribute("experiencedTutors");
            TutorDAO tutorDAO = new TutorDAO();
            StudenteDAO studenteDAO = new StudenteDAO();

            if(tutorDAO.doRetrieveByEmail(userEmail) != null){ //se è un tutor
                tutorDAO.doEditName(userEmail, newFirstName, newLastName); //si modifica nel db
                if(list1 != null)
                    for(Tutor t : list1)
                        if(t.getEmailUtente().equals(userEmail)){ //si modifica se è presente nella tutor-list
                            t.setNome(newFirstName);
                            t.setCognome(newLastName);
                        }
                if(list2 != null)
                    for(Tutor t : list2)
                        if(t.getEmailUtente().equals(userEmail)){ //si modifica se è presente nella experiencedTutors-list
                            t.setNome(newFirstName);
                            t.setCognome(newLastName);
                        }
            }
            if(studenteDAO.doRetrieveByEmail(userEmail) != null) //se è uno studente
                studenteDAO.doEditName(userEmail, newFirstName, newLastName); //si modifica solo nel db
            destination = "admin-data";
        } else destination = "/WEB-INF/errors/error500.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }
}
