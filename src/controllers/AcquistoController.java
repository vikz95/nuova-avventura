package controllers;

import business.*;
import data.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.YearMonth;
import java.util.ArrayList;

@WebServlet("/AcquistoController")
public class AcquistoController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "AGGIUNGI_CARRELLO":
                aggiungiNelCarrello(request, response);
                break;
            case "AGGIORNA_CARRELLO":
                aggiornaQuantita(request, response);
                break;
            case "EFFETTUA_ACQUISTO":
                effettuaAcquisto(request, response);
                break;
        }
    }

    private void aggiungiNelCarrello(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        int quantita = Integer.parseInt(request.getParameter("quantita"));
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            try {
                CarrelloDB.aggiungi(idProdotto, quantita, idUtente);
                mostraCarrello(request, response);
            } catch (SQLIntegrityConstraintViolationException e) {
                request.setAttribute("PRODUCT_ERROR", "Hai già aggiunto questo prodotto al carrello");
                mostraProdotto(request, response, idProdotto);
            }
        } else {
            request.setAttribute("PRODUCT_ERROR", "Devi prima effettuare l'accesso per aggiungere prodotti al carrello");
            mostraProdotto(request, response, idProdotto);
        }
    }

    private void mostraProdotto(HttpServletRequest request, HttpServletResponse response, int idProdotto) throws ServletException, IOException {
        Prodotto prodotto = ProdottoDB.getProdotto(idProdotto);
        request.setAttribute("prodotto", prodotto);
        request.setAttribute("recensioni", RecensioneDB.getRecensioni(idProdotto));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/prodotto.jsp");
        dispatcher.forward(request, response);
    }

    private void aggiornaQuantita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        int quantita = Integer.parseInt(request.getParameter("quantita"));
        CarrelloDB.updateQuantita(idProdotto, quantita);
        mostraCarrello(request, response);
    }

    private void effettuaAcquisto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setNome(request.getParameter("nome"));
            indirizzo.setCognome(request.getParameter("cognome"));
            indirizzo.setIndirizzo(request.getParameter("indirizzo"));
            indirizzo.setCitta(request.getParameter("citta"));
            indirizzo.setCAP(request.getParameter("cap"));
            indirizzo.setProvincia(request.getParameter("provincia"));
            int idIndirizzo = IndirizzoDB.salvaIndirizzo(indirizzo);

            String tipoSpedizione = request.getParameter("spedizione");
            int idSpedizione = tipoSpedizione.equals("posta") ? 1 : 2;

            CartaCredito cartaCredito = new CartaCredito();
            cartaCredito.setIntestatario(request.getParameter("intestatario"));
            cartaCredito.setNumero(request.getParameter("numeroCarta"));
            cartaCredito.setDataScadenza(YearMonth.parse(request.getParameter("scadenza")));
            cartaCredito.setCodiceSicurezza(request.getParameter("codiceSicurezza"));
            int idCarta = CartaCreditoDB.salvaCarta(cartaCredito, idUtente);

            try {
                OrdineDB.creaOrdine(idUtente, idIndirizzo, idSpedizione, idCarta);
                visualizzaOrdini(request, response);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals("Quantita non sufficiente")) {
                    mostraCarrello(request, response);
                }
            }
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "MOSTRA_CARRELLO":
                mostraCarrello(request, response);
                break;
            case "CANCELLA_CARRELLO":
                cancellaProdotto(request, response);
                break;
            case "GOTO_CHECKOUT":
                goToCheckout(request, response);
                break;
            case "VISUALIZZA_ORDINI":
                visualizzaOrdini(request, response);
                break;
            case "VISUALIZZA_ORDINE":
                visualizzaOrdine(request, response);
                break;
            case "ANNULLA_ORDINE":
                annullaOrdine(request, response);
                break;
        }
    }

    private void mostraCarrello(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            ArrayList<ProdottoCarrello> prodottiCarrello = CarrelloDB.getProdottiCarrello(idUtente);
            request.setAttribute("prodottiCarrello", prodottiCarrello);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/carrello.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void cancellaProdotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        CarrelloDB.cancellaProdotto(idProdotto);
        mostraCarrello(request, response);
    }

    private void goToCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            request.setAttribute("indirizzo", IndirizzoDB.getIndirizzoByUtente(idUtente));
            request.setAttribute("posta", TipologieSpedizioniDB.getSpedizione("posta"));
            request.setAttribute("corriere", TipologieSpedizioniDB.getSpedizione("corriere"));
            request.setAttribute("carta", CartaCreditoDB.getCartabyUtente(idUtente));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void visualizzaOrdini(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            ArrayList<Ordine> ordini = OrdineDB.getOrdini(idUtente);
            request.setAttribute("ordini", ordini);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/ordini.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void visualizzaOrdine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
            Ordine ordine = OrdineDB.getOrdine(idOrdine);
            request.setAttribute("ordine", ordine);
            request.setAttribute("indirizzo", IndirizzoDB.getIndirizzo(ordine.getIdIndirizzo()));
            request.setAttribute("spedizione", TipologieSpedizioniDB.getSpedizione(ordine.getIdSpedizione()));
            Pagamento pagamento = PagamentoDB.getPagamento(ordine.getIdPagamento());
            request.setAttribute("pagamento", pagamento);
            request.setAttribute("carta", CartaCreditoDB.getCarta(pagamento.getIdCarta()));
            ArrayList<ProdottoOrdine> prodottiOrdine = OrdineDB.getProdottiOrdine(idOrdine);
            request.setAttribute("prodottiOrdine", prodottiOrdine);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/ordine.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void annullaOrdine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUtente = (Integer) request.getSession(false).getAttribute("idUtente");
        if (idUtente != null) {
            int idOrdine = Integer.parseInt(request.getParameter("id"));
            if (OrdineDB.annullaOrdine(idOrdine)) {
                visualizzaOrdini(request, response);
            } else {
                request.setAttribute("ORDINE_ERROR", "Non puoi annullare l'ordine perché è già stato processato");
                visualizzaOrdine(request, response);
            }
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}
