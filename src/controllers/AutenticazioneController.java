package controllers;

import business.Indirizzo;
import business.Utente;
import data.UtenteDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/AutenticazioneController")
public class AutenticazioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "LOGIN":
                login(request, response);
                break;
            case "SIGNUP":
                signup(request, response);
                break;
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Integer idUtente = UtenteDB.accessoUtente(email, password);
        if (idUtente != null) {
            request.getSession().setAttribute("idUtente", idUtente);
            response.sendRedirect("NavigazioneController?command=MOSTRA_HOME");
        } else {
            request.setAttribute("LOGIN_ERROR", "L'email o la password non sono corretti");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setNome(request.getParameter("nome"));
        indirizzo.setCognome(request.getParameter("cognome"));
        indirizzo.setProvincia(request.getParameter("provincia"));
        indirizzo.setCitta(request.getParameter("citta"));
        indirizzo.setCAP(request.getParameter("cap"));
        indirizzo.setIndirizzo(request.getParameter("indirizzo"));

        Utente utente = new Utente();
        utente.setNome(request.getParameter("nome"));
        utente.setCognome(request.getParameter("cognome"));
        utente.setEmail(request.getParameter("email"));
        utente.setPassword(request.getParameter("password"));
        utente.setDataNascita(Date.valueOf(request.getParameter("dataNascita")));
        utente.setTelefono(request.getParameter("telefono"));
        utente.setResidenza(indirizzo);
        try {
            UtenteDB.registraUtente(utente, indirizzo);
            response.sendRedirect("login.jsp");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("SIGNUP_ERROR", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/signup.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "LOGOUT":
                logout(request, response);
                break;
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("login.jsp");
    }
}
