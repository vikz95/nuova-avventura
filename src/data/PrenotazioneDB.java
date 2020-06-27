package data;

import business.Prenotazione;
import business.PrenotazioniProdotto;

import java.sql.*;
import java.util.ArrayList;

public class PrenotazioneDB {

    public static void prenota(Prenotazione prenotazione) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO prenotazioni (utente, prodotto, quantita) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, prenotazione.getIdUtente());
            ps.setInt(2, prenotazione.getIdProdotto());
            ps.setInt(3, prenotazione.getQuantita());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static ArrayList<PrenotazioniProdotto> getPrenotazioni() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<PrenotazioniProdotto> prenotazioniProdotti = new ArrayList<>();
        try {
            String query = "SELECT prodotto, COUNT(quantita) AS prenotazioni_totali FROM prenotazioni GROUP BY prodotto";
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                PrenotazioniProdotto pp = new PrenotazioniProdotto();
                pp.setP(ProdottoDB.getProdotto(rs.getInt("prodotto")));
                pp.setQuantita(rs.getInt("prenotazioni_totali"));
                prenotazioniProdotti.add(pp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prenotazioniProdotti;
    }

    public static void cancellaPrenotati(int idProdotto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        try {
            String query = "DELETE FROM prenotazioni WHERE prodotto = " + idProdotto;
            s = connection.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            pool.freeConnection(connection);
        }
    }
}
