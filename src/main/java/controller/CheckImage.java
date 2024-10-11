package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckImage", value = "/check-image")
public class CheckImage extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            return;
        response.setContentType("application/json");

        // Recupero del path dell'immagine
        String imagePath = request.getParameter("imagePath");
        File imageFile = new File(request.getServletContext().getRealPath(imagePath));

        // Conversione del risultato in JSON, utilizzando GSON
        Gson gson = new Gson();
        String suggestionsJSON = gson.toJson(imageFile.exists());

        // Scrittura nel writer della risposta per passare i dati al Javascript, chiudendolo successivamente
        PrintWriter writer = response.getWriter();
        writer.write(suggestionsJSON);
        writer.close();
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }
}
