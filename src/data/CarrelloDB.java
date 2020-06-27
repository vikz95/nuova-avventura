package data;

import business.ProdottoCarrello;

import java.sql.*;
import java.util.ArrayList;

public class CarrelloDB {


    public static void aggiungi(int idProdotto, int quantita, int idUtente) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO carrelli (prodotto, utente, quantita) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, idProdotto);
            ps.setInt(2, idUtente);
            ps.setInt(3, quantita);
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

    public static ArrayList<ProdottoCarrello> getProdottiCarrello(Integer idUtente) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<ProdottoCarrello> prodottiCarrello = new ArrayList<>();
        try {
            String query = "SELECT * FROM carrelli WHERE utente = " + idUtente;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                ProdottoCarrello pc = new ProdottoCarrello();
                pc.setQuantita(rs.getInt("quantita"));
                pc.setProdotto(ProdottoDB.getProdotto(rs.getInt("prodotto")));
                prodottiCarrello.add(pc);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodottiCarrello;
    }

    public static void updateQuantita(int idProdotto, int quantita) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            String query = "UPDATE carrelli SET quantita = ? WHERE prodotto = " + idProdotto;
            ps = connection.prepareStatement(query);
            ps.setInt(1, quantita);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static void cancellaProdotto(int idProdotto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            String query = "DELETE FROM carrelli WHERE prodotto = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, idProdotto);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
}
