package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Studente;
import model.Tutor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@MultipartConfig
@WebServlet(name = "ImageServlet", value = "/image-servlet")
public class ImageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Recupero del file e del tutor relativo ad esso
        Part filePart = request.getPart("file");
        Tutor tutor = (Tutor) request.getSession().getAttribute("tutor");
        Studente studente = (Studente) request.getSession().getAttribute("studente");
        String email = "";
        if (tutor != null)
            email = tutor.getEmailUtente().replaceAll("[^a-zA-Z0-9]", "_");// Sostituzione di tutti i caratteri speciali che non sono lettere o numeri con un underscore
        else if (studente != null)
            email = studente.getEmailUtente().replaceAll("[^a-zA-Z0-9]", "_");// Sostituzione di tutti i caratteri speciali che non sono lettere o numeri con un underscore

        // Estrazione dell'estensione del file originale
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        // Creazione del nome del file utilizzando l'email del tutor
        String newFileName = email + fileExtension;

        // Lettura dei dati dell'immagine in un array di byte
        InputStream fileInputStream = filePart.getInputStream();
        byte[] imageData = fileInputStream.readAllBytes();

        // Salvataggio dell'immagine nella cartella "target" (per Tomcat)
        String targetPath = getServletContext().getRealPath("/WEB-INF/classes/" + CARTELLA + File.separator + newFileName);
        Path targetImagePath = Paths.get(targetPath);
        Files.createDirectories(targetImagePath.getParent());
        Files.write(targetImagePath,imageData);

        // Salvataggio dell'immagine nella cartella "src/main/webapp/propics_by_email" (per IntelliJ)
        String webappPath = getServletContext().getRealPath("/") + CARTELLA + File.separator + newFileName;
        Path webappImagePath = Paths.get(webappPath);
        Files.createDirectories(webappImagePath.getParent());
        Files.write(webappImagePath,imageData);

        RequestDispatcher dispatcher = request.getRequestDispatcher("profile-servlet");
        dispatcher.forward(request,response);
    }

    public static final String CARTELLA = "propics_by_email";
}
