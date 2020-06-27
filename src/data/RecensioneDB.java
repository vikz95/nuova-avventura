package data;

import business.Recensione;

import java.sql.*;
import java.util.ArrayList;

public class RecensioneDB {

    public static void salvaRecensione(Recensione recensione, int idUtente, int idProdotto) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);

            String query = "INSERT INTO recensioni (stelle, nickname, testo, data, di_utente, per_prodotto) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, recensione.getStelle());
            ps.setString(2, recensione.getNickname());
            ps.setString(3, recensione.getTesto());
            ps.setDate(4, recensione.getData());
            ps.setInt(5, idUtente);
            ps.setInt(6, idProdotto);
            ps.executeUpdate();

            query = "SELECT COUNT(per_prodotto) AS numero_recensioni FROM recensioni " +
                    "WHERE per_prodotto = " + idProdotto;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            int numeroRecensioni = rs.getInt("numero_recensioni");

            query = "UPDATE prodotti " +
                    "SET media_recensioni = media_recensioni + ((? - media_recensioni) / ?) " +
                    "WHERE id = " + idProdotto;
            ps = connection.prepareStatement(query);
            ps.setInt(1, recensione.getStelle());
            ps.setInt(2, numeroRecensioni);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
    }

    public static ArrayList<Recensione> getRecensioni(int idProdotto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<Recensione> recnsioni = new ArrayList<>();
        try {
            String query = "SELECT * FROM recensioni WHERE per_prodotto = " + idProdotto + " ORDER BY data DESC";
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Recensione r = new Recensione();
                r.setStelle(rs.getInt("stelle"));
                r.setNickname(rs.getString("nickname"));
                r.setTesto(rs.getString("testo"));
                r.setData(rs.getDate("data"));
                recnsioni.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return recnsioni;
    }
}
