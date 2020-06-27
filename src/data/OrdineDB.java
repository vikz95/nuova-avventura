package data;

import business.Ordine;
import business.ProdottoOrdine;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class OrdineDB {

    public static Ordine getOrdine(Integer idOrdine) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Ordine o = new Ordine();
        Statement s = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM ordini WHERE id = " + idOrdine;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            o.setId(rs.getInt("id"));
            o.setDataCreazione(rs.getDate("data_creazione"));
            o.setDataSpedizione((rs.getDate("data_spedizione")));
            o.setStato(Ordine.StatoOrdine.valueOf(rs.getString("stato")));
            o.setIdUtente(rs.getInt("utente"));
            o.setIdIndirizzo(rs.getInt("indirizzo_spedizione"));
            o.setIdSpedizione(rs.getInt("tipo_spedizione"));
            o.setIdPagamento(rs.getInt("pagamento"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return o;
    }

    public static ArrayList<Ordine> getOrdini(Integer idUtente) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        ArrayList<Ordine> ordini = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM ordini WHERE utente = " + idUtente + " ORDER BY data_creazione DESC, id DESC";
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Ordine o = new Ordine();
                o.setId(rs.getInt("id"));
                o.setDataCreazione(rs.getDate("data_creazione"));
                o.setStato(Ordine.StatoOrdine.valueOf(rs.getString("stato")));
                ordini.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return ordini;
    }

    public static ArrayList<Ordine> getOrdini() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        ArrayList<Ordine> ordini = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM ordini ORDER BY data_creazione DESC, id DESC";
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Ordine o = new Ordine();
                o.setId(rs.getInt("id"));
                o.setDataCreazione(rs.getDate("data_creazione"));
                o.setStato(Ordine.StatoOrdine.valueOf(rs.getString("stato")));
                o.setIdUtente(rs.getInt("utente"));
                ordini.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return ordini;
    }

    public static ArrayList<ProdottoOrdine> getProdottiOrdine(int idOrdine) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<ProdottoOrdine> prodottiOrdine = new ArrayList<>();
        try {
            String query = "SELECT * FROM prodottiordini WHERE ordine = " + idOrdine;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                ProdottoOrdine po = new ProdottoOrdine();
                po.setQuantita(rs.getInt("quantita"));
                po.setProdotto(ProdottoDB.getProdotto(rs.getInt("prodotto")));
                prodottiOrdine.add(po);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodottiOrdine;
    }

    public static void creaOrdine(Integer idUtente, int idIndirizzo, int idSpedizione, int idCarta) throws IllegalArgumentException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            int idPagamento = PagamentoDB.creaPagamento(idCarta, connection);

            String query = "INSERT into ordini (data_creazione, stato, utente, indirizzo_spedizione, tipo_spedizione, pagamento) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, Ordine.StatoOrdine.IN_CORSO.name());
            ps.setInt(3, idUtente);
            ps.setInt(4, idIndirizzo);
            ps.setInt(5, idSpedizione);
            ps.setInt(6, idPagamento);
            ps.executeUpdate();

            String queryId = "SELECT LAST_INSERT_ID() AS last_id;";
            s = connection.createStatement();
            rs = s.executeQuery(queryId);
            rs.next();
            int idOrdine = rs.getInt("last_id");

            daCarrelloAOrdine(idOrdine, idUtente, connection);
            BigDecimal importo = calcolaImporto(idOrdine, idSpedizione, connection);
            PagamentoDB.setImporto(idPagamento, importo, connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
    }

    private static void daCarrelloAOrdine(int idOrdine, Integer idUtente, Connection connection) throws SQLException, IllegalArgumentException {
        String query = "SELECT prodotto, quantita FROM carrelli WHERE utente = " + idUtente;
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            int idProdotto = rs.getInt("prodotto");
            int quantitaDesiderata = rs.getInt("quantita");

            if (quantitaSufficiente(idProdotto, quantitaDesiderata, connection)) {
                decrementaQuantitaProdotto(idProdotto, quantitaDesiderata, connection);
                String query1 = "INSERT INTO prodottiordini (prodotto, ordine, quantita) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query1);
                ps.setInt(1, idProdotto);
                ps.setInt(2, idOrdine);
                ps.setInt(3, quantitaDesiderata);
                ps.executeUpdate();
                DBUtil.closePreparedStatement(ps);

                String query2 = "DELETE FROM carrelli WHERE prodotto = ? AND utente = ?";
                ps = connection.prepareStatement(query2);
                ps.setInt(1, idProdotto);
                ps.setInt(2, idUtente);
                ps.executeUpdate();
                DBUtil.closePreparedStatement(ps);
            } else {
                throw new IllegalArgumentException("Quantita non sufficiente");
            }
        }
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
    }

    private static boolean quantitaSufficiente(int idProdotto, int quantitaDesiderata, Connection connection) throws SQLException {
        String query = "SELECT quantita FROM prodotti WHERE id = " + idProdotto;
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);
        rs.next();
        int quantitaDisponibile = rs.getInt("quantita");
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return quantitaDisponibile >= quantitaDesiderata;
    }

    private static void decrementaQuantitaProdotto(int idProdotto, int quantitaDesiderata, Connection connection) throws SQLException {
        String query = "UPDATE prodotti SET quantita = quantita - ? WHERE id = " + idProdotto;
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, quantitaDesiderata);
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static BigDecimal calcolaImporto(int idOrdine, int idSpedizione, Connection connection) throws SQLException {
        BigDecimal importo = BigDecimal.ZERO;

        String query = "SELECT costo FROM tipologiespedizioni WHERE id = " + idSpedizione;
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);
        rs.next();
        BigDecimal costoSpedizione = rs.getBigDecimal("costo");

        query = "SELECT prodotto, quantita FROM prodottiordini WHERE ordine = " + idOrdine;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            int quantita = rs.getInt("quantita");
            int idProdotto = rs.getInt("prodotto");
            BigDecimal costoProdotto = getCostoProdotto(idProdotto, connection);
            importo = importo.add(costoProdotto.multiply(new BigDecimal(quantita)));
        }
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return importo.add(costoSpedizione);
    }

    private static BigDecimal getCostoProdotto(int idProdotto, Connection connection) throws SQLException {
        String query = "SELECT costo FROM prodotti WHERE id = " + idProdotto;
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);
        rs.next();
        BigDecimal costoProdotto = rs.getBigDecimal("costo");
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return costoProdotto;
    }

    public static boolean annullaOrdine(int idOrdine) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        boolean annullato = false;
        try {
            connection.setAutoCommit(false);
            String query = "SELECT stato FROM ordini WHERE id = " + idOrdine;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            if (Ordine.StatoOrdine.valueOf(rs.getString("stato")) == Ordine.StatoOrdine.IN_CORSO) {
                setStato(idOrdine, Ordine.StatoOrdine.ANNULLATO, connection);
                annullato = true;
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return annullato;
    }

    public static void segnaSpedito(int idOrdine) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            setStato(idOrdine, Ordine.StatoOrdine.SPEDITO, connection);
            String query = "UPDATE ordini SET data_spedizione = ? WHERE id = " + idOrdine;
            ps = connection.prepareStatement(query);
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            pool.freeConnection(connection);
            DBUtil.closePreparedStatement(ps);
        }
    }

    public static void setStato(int idOrdine, Ordine.StatoOrdine stato, Connection connection) throws SQLException {
        String query = "UPDATE ordini SET stato = ? WHERE id = " + idOrdine;
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, stato.name());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }
}
