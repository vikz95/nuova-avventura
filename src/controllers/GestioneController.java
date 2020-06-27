package controllers;

import business.*;
import data.*;
import util.MailUtilGmail;
import util.PaymentSimulator;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.util.ArrayList;

@WebServlet("/admin/GestioneController")
@MultipartConfig
public class GestioneController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        switch (command) {
            case "AGGIORNA_SPEDIZIONI":
                aggiornaSpedizioni(request, response);
                break;
            case "AGGIUNGI_PRODOTTO":
                aggiungiProdotto(request, response);
                break;
            case "MODIFICA_PRODOTTO":
                modificaProdotto(request, response);
                break;
            case "RICEVI_PAGAMENTO":
                riceviPagamento(request, response);
                break;
        }
    }

    private void aggiornaSpedizioni(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TipologiaSpedizione posta = new TipologiaSpedizione();
        posta.setTipo("posta");
        posta.setCosto(new BigDecimal(request.getParameter("posta")));
        TipologieSpedizioniDB.updateCosto(posta);

        TipologiaSpedizione corriere = new TipologiaSpedizione();
        corriere.setTipo("corriere");
        corriere.setCosto(new BigDecimal(request.getParameter("corriere")));
        TipologieSpedizioniDB.updateCosto(corriere);
        visualizzaSpedizioni(request, response);
    }

    private void aggiungiDettagliProdotto(HttpServletRequest request, Prodotto prodotto) {
        switch (prodotto.getCategoria()) {
            case LIBRI:
                prodotto.setDettagliProdotto(estraiLibro(request));
                break;
            case ALBUM:
                prodotto.setDettagliProdotto(estraiAlbum(request));
                break;
            case FILM:
                prodotto.setDettagliProdotto(estraiFilm(request));
                break;
            case VIDEOGIOCHI:
                prodotto.setDettagliProdotto(estraiVideogioco(request));
                break;
        }
    }

    private void aggiungiProdotto(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Prodotto prodotto = estraiProdotto(request);
            aggiungiDettagliProdotto(request, prodotto);
            ProdottoDB.aggiungiProdotto(prodotto);
            request.setAttribute("PRODUCT_SUCCESS", "Il prodotto è stato aggiunto con successo");
        } catch (SQLIntegrityConstraintViolationException e) {
            request.setAttribute("PRODUCT_ERROR", "Il codice SKU è già stato assegnato a un prodotto, scegline uno nuovo.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("PRODUCT_ERROR", e.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/aggiungi-prodotto.jsp");
        dispatcher.forward(request, response);
    }

    private void modificaProdotto(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Prodotto prodotto = estraiProdotto(request);
            prodotto.setId(Integer.parseInt(request.getParameter("id")));
            aggiungiDettagliProdotto(request, prodotto);
            ProdottoDB.updateProdotto(prodotto);
            if (Integer.parseInt(request.getParameter("vecchiaQuantita")) == 0) {
                inviaMail(prodotto.getId(), prodotto.getTitolo());
                PrenotazioneDB.cancellaPrenotati(prodotto.getId());
            }
            elencaProdotti(request, response);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                request.setAttribute("PRODUCT_ERROR", "Il codice SKU è già stato assegnato a un prodotto, scegline uno nuovo.");
            } else if (e instanceof IllegalArgumentException) {
                request.setAttribute("PRODUCT_ERROR", e.getMessage());
            }
            getProdotto(request, response);
        }
    }

    private void inviaMail(int idProdotto, String titolo) {
        ArrayList<Utente> utentiPrenotati = UtenteDB.getUtentiPrenotati(idProdotto);
        for (Utente u : utentiPrenotati) {
            String to = u.getEmail().trim();
            String from = "nuovaavventura.sales@gmail.com";
            String subject = "Prodotto nuovamente disponibile";
            String body = "Gentile " + u.getNome() + " " + u.getCognome() + " la informiamo che il prodotto " +
                    "<a href=\"http://localhost:8080/NuovaAvventura/NavigazioneController?command=MOSTRA_PRODOTTO&id="
                    + idProdotto + "\">" + titolo + "</a> da lei prenotato è nuovamente disponibile.\n\n " +
                    "Lo staff di Nuova Avventura";
            try {
                MailUtilGmail.sendMail(to, from, subject, body, true);
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Prodotto estraiProdotto(HttpServletRequest request) throws IOException, ServletException {
        Prodotto prodotto = new Prodotto();
        prodotto.setTitolo(request.getParameter("titolo"));
        prodotto.setFoto(request.getPart("foto").getInputStream());
        prodotto.setDescrizione(request.getParameter("descrizione"));
        prodotto.setSku(request.getParameter("sku"));
        prodotto.setAnno(Integer.parseInt(request.getParameter("anno")));
        prodotto.setQuantita(Integer.parseInt(request.getParameter("quantita")));
        prodotto.setCosto(new BigDecimal(request.getParameter("costo")));
        prodotto.setDataCaricamento(new Date(System.currentTimeMillis()));
        prodotto.setCategoria(Prodotto.Categoria.valueOf(request.getParameter("categoria")));
        return prodotto;
    }

    private Libro estraiLibro(HttpServletRequest request) {
        Libro libro = new Libro();
        libro.setAutori(request.getParameter("autori"));
        libro.setEditore(request.getParameter("editore"));
        libro.setFumetto(request.getParameter("fumetto").equals("si"));
        libro.setNumeroPagine(Integer.parseInt(request.getParameter("pagine")));
        return libro;
    }

    private Album estraiAlbum(HttpServletRequest request) {
        Album album = new Album();
        album.setAutori(request.getParameter("autori"));
        album.setEtichetta(request.getParameter("etichetta"));
        album.setNumeroSupporti(Integer.parseInt(request.getParameter("numeroSupporti")));
        album.setSupporto(Album.SupportoAlbum.valueOf(request.getParameter("supporto")));
        int numCanzone = 1;
        while (request.getParameter("canzone-" + numCanzone) != null) {
            Canzone canzone = new Canzone();
            canzone.setPosizione(numCanzone);
            canzone.setTitolo(request.getParameter("canzone-" + numCanzone));
            canzone.setDurata(Time.valueOf(request.getParameter("durata-" + numCanzone)));
            album.aggiungiCanzone(canzone);
            numCanzone++;
        }
        return album;
    }

    private Film estraiFilm(HttpServletRequest request) throws IllegalArgumentException {
        String attori = request.getParameter("attori");
        int numAttori = attori.split(",").length;
        if (numAttori >= 3 && numAttori <= 6) {
            Film film = new Film();
            film.setAttori(attori);
            film.setRegia(request.getParameter("regia"));
            film.setPaese(request.getParameter("paese"));
            film.setSupporto(Film.SupportoFilm.valueOf(request.getParameter("supporto")));
            return film;
        } else {
            throw new IllegalArgumentException("Ci devono essere min 3 max 6 attori, tu ne hai inseriti " + numAttori);
        }
    }

    private Videogioco estraiVideogioco(HttpServletRequest request) {
        Videogioco videogioco = new Videogioco();
        videogioco.setProduttore(request.getParameter("produttore"));
        videogioco.setPiattaforma(request.getParameter("piattaforma"));
        videogioco.setEtaConsigliata(Integer.parseInt(request.getParameter("etaConsigliata")));
        return videogioco;
    }

    private void riceviPagamento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idPagamento = Integer.parseInt(request.getParameter("idPagamento"));
        int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
        Pagamento.StatoPagamento stato = PaymentSimulator.pay();
        PagamentoDB.setStato(idPagamento, idOrdine, stato);
        elencaOrdini(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "VISUALIZZA_SPEDIZIONI":
                visualizzaSpedizioni(request, response);
                break;
            case "ELENCA_PRODOTTI":
                elencaProdotti(request, response);
                break;
            case "GET_PRODOTTO":
                getProdotto(request, response);
                break;
            case "CANCELLA_PRODOTTO":
                cancellaProdotto(request, response);
                break;
            case "ELENCA_PRENOTAZIONI":
                elencaPrenotazioni(request, response);
                break;
            case "ELENCA_PAGAMENTI":
                elencaPagamenti(request, response);
                break;
            case "ELENCA_ORDINI":
                elencaOrdini(request, response);
                break;
            case "GET_ORDINE":
                getOrdine(request, response);
                break;
            case "ANNULLA_ORDINE":
                annullaOrdine(request, response);
                break;
            case "SPEDISCI_ORDINE":
                spedisciOrdine(request, response);
                break;
        }
    }


    private void visualizzaSpedizioni(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TipologiaSpedizione posta = TipologieSpedizioniDB.getSpedizione("posta");
        TipologiaSpedizione corriere = TipologieSpedizioniDB.getSpedizione("corriere");

        request.setAttribute("posta", posta);
        request.setAttribute("corriere", corriere);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/spedizioni.jsp");
        dispatcher.forward(request, response);
    }

    private void elencaProdotti(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        ArrayList<Prodotto> prodotti = ProdottoDB.getProdottiByCategoria(categoria);
        request.setAttribute("prodotti", prodotti);
        request.setAttribute("categoria", categoria);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/prodotti.jsp");
        dispatcher.forward(request, response);
    }

    private void getProdotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Prodotto p = ProdottoDB.getProdotto(id);
        request.setAttribute("prodotto", p);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/modifica-prodotto.jsp");
        dispatcher.forward(request, response);
    }

    private void cancellaProdotto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProdottoDB.cancellaProdotto(id);
        elencaProdotti(request, response);
    }

    private void elencaPrenotazioni(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<PrenotazioniProdotto> prenotazioniProdotti = PrenotazioneDB.getPrenotazioni();
        request.setAttribute("prenotazioniProdotti", prenotazioniProdotti);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/prenotazioni.jsp");
        dispatcher.forward(request, response);
    }

    private void elencaPagamenti(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Pagamento> pagamenti = PagamentoDB.getPagamenti();
        request.setAttribute("pagamenti", pagamenti);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/pagamenti.jsp");
        dispatcher.forward(request, response);
    }

    private void elencaOrdini(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Ordine> ordini = OrdineDB.getOrdini();
        request.setAttribute("ordini", ordini);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/ordini.jsp");
        dispatcher.forward(request, response);
    }

    private void getOrdine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrdine = Integer.parseInt(request.getParameter("id"));
        Ordine ordine = OrdineDB.getOrdine(idOrdine);
        request.setAttribute("ordine", ordine);
        request.setAttribute("indirizzo", IndirizzoDB.getIndirizzo(ordine.getIdIndirizzo()));
        request.setAttribute("spedizione", TipologieSpedizioniDB.getSpedizione(ordine.getIdSpedizione()));
        Pagamento pagamento = PagamentoDB.getPagamento(ordine.getIdPagamento());
        request.setAttribute("pagamento", pagamento);
        request.setAttribute("carta", CartaCreditoDB.getCarta(pagamento.getIdCarta()));
        ArrayList<ProdottoOrdine> prodottiOrdine = OrdineDB.getProdottiOrdine(idOrdine);
        request.setAttribute("prodottiOrdine", prodottiOrdine);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/ordine.jsp");
        dispatcher.forward(request, response);
    }

    private void annullaOrdine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrdine = Integer.parseInt(request.getParameter("id"));
        if (OrdineDB.annullaOrdine(idOrdine)) {
            elencaOrdini(request, response);
        }
    }

    private void spedisciOrdine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrdine = Integer.parseInt(request.getParameter("id"));
        OrdineDB.segnaSpedito(idOrdine);
        elencaOrdini(request, response);
    }
}
