package controllers;

import business.Prenotazione;
import business.Prodotto;
import business.Recensione;
import data.PrenotazioneDB;
import data.ProdottoDB;
import data.RecensioneDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

@WebServlet(value = "/NavigazioneController")
public class NavigazioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "SCRIVI_RECENSIONE":
                scriviRecensione(request, response);
                break;
            case "PRENOTA":
                prenota(request, response);
                break;
        }
    }

    private void scriviRecensione(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Recensione recensione = new Recensione();
        recensione.setStelle(Integer.parseInt(request.getParameter("rating")));
        recensione.setNickname(request.getParameter("nickname"));
        recensione.setTesto(request.getParameter("testo"));
        recensione.setData(new Date(System.currentTimeMillis()));
        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            try {
                RecensioneDB.salvaRecensione(recensione, idUtente, idProdotto);
                request.setAttribute("PRODUCT_SUCCESS", "La recensione è stata aggiunta");
            } catch (SQLIntegrityConstraintViolationException e) {
                request.setAttribute("PRODUCT_ERROR", "Hai già recensito questo prodotto in passato");
            }
        } else {
            request.setAttribute("PRODUCT_ERROR", "Devi prima effettuare l'accesso per poter recensire il prodotto");
        }
        Prodotto prodotto = ProdottoDB.getProdotto(idProdotto);
        request.setAttribute("prodotto", prodotto);
        request.setAttribute("recensioni", RecensioneDB.getRecensioni(idProdotto));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prodotto.jsp");
        dispatcher.forward(request, response);
    }

    private void prenota(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setIdProdotto(Integer.parseInt(request.getParameter("idProdotto")));
        prenotazione.setQuantita(Integer.parseInt(request.getParameter("quantita")));
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            prenotazione.setIdUtente(idUtente);
            try {
                PrenotazioneDB.prenota(prenotazione);
                request.setAttribute("PRODUCT_SUCCESS", "Prenotazione effettuata con successo");
            } catch (SQLIntegrityConstraintViolationException e) {
                request.setAttribute("PRODUCT_ERROR", "Hai già prenotato questo prodotto");
            }
        } else {
            request.setAttribute("PRODUCT_ERROR", "Devi prima effettuare l'accesso per poter prenotare il prodotto");
        }
        Prodotto prodotto = ProdottoDB.getProdotto(prenotazione.getIdProdotto());
        request.setAttribute("prodotto", prodotto);
        request.setAttribute("recensioni", RecensioneDB.getRecensioni(prenotazione.getIdProdotto()));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prodotto.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if (command == null) {
            command = "MOSTRA_HOME";
        }
        switch (command) {
            case "MOSTRA_HOME":
                mostraHome(request, response);
                break;
            case "MOSTRA_PRODOTTO":
                mostraProdotto(request, response);
                break;
            case "MOSTRA_PRODOTTI":
                mostraProdotti(request, response);
                break;
            case "RISULTATI_RICERCA":
                mostraRisultatiRicerca(request, response);
            default:
                mostraHome(request, response);
        }
    }

    private void mostraHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("libri", ProdottoDB.getProdottiByCategoriaAndLimit("LIBRI", 4));
        request.setAttribute("album", ProdottoDB.getProdottiByCategoriaAndLimit("ALBUM", 4));
        request.setAttribute("film", ProdottoDB.getProdottiByCategoriaAndLimit("FILM", 4));
        request.setAttribute("videogiochi", ProdottoDB.getProdottiByCategoriaAndLimit("VIDEOGIOCHI", 4));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    private void mostraProdotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Prodotto prodotto = ProdottoDB.getProdotto(id);
        request.setAttribute("prodotto", prodotto);
        request.setAttribute("recensioni", RecensioneDB.getRecensioni(id));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prodotto.jsp");
        dispatcher.forward(request, response);
    }

    private void mostraProdotti(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        String tipo = request.getParameter("tipo");
        String ordinamento = request.getParameter("ordinamento");
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        if (tipo == null && ordinamento == null) {
            prodotti = ProdottoDB.getProdottiByCategoria(categoria);
        } else if (ordinamento == null) {
            prodotti = ProdottoDB.getProdottiByCategoriaAndTipo(categoria, tipo);
        } else if (tipo == null) {
            prodotti = ProdottoDB.getProdottiByCategoriaAndOrdinamento(categoria, ordinamento);
        }
        request.setAttribute("prodotti", prodotti);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prodotti.jsp");
        dispatcher.forward(request, response);
    }

    private void mostraRisultatiRicerca(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        String termini = request.getParameter("termini");
        ArrayList<Prodotto> prodotti;
        if (categoria.equals("")) {
            prodotti = ProdottoDB.getProdottiByTerminiDiRicerca(termini);
        } else {
            prodotti = ProdottoDB.getProdottiByTerminiDiRicercaAndCategoria(termini, categoria);
        }
        request.setAttribute("prodotti", prodotti);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/ricerca.jsp");
        dispatcher.forward(request, response);
    }
}
