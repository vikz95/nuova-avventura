package data;

import business.CartaCredito;

import java.sql.*;
import java.time.YearMonth;

public class CartaCreditoDB {

    public static CartaCredito getCarta(Integer idCarta) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        CartaCredito cc = new CartaCredito();
        try {
            String query = "SELECT * FROM cartedicredito WHERE id = " + idCarta;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            if (rs.next()) {
                cc.setId(rs.getInt("id"));
                cc.setIntestatario(rs.getString("intestatario"));
                cc.setNumero(rs.getString("numero"));
                cc.setCodiceSicurezza(rs.getString("codice_sicurezza"));
                cc.setDataScadenza(YearMonth.of(rs.getInt("anno_scadenza"), rs.getInt("mese_scadenza")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return cc;
    }

    public static CartaCredito getCartabyUtente(Integer idUtente) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        CartaCredito cc = new CartaCredito();
        try {
            String query = "SELECT * FROM cartedicredito JOIN utenti u on cartedicredito.id = u.carta " +
                    "WHERE u.id = " + idUtente;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            if (rs.next()) {
                cc.setId(rs.getInt("id"));
                cc.setIntestatario(rs.getString("intestatario"));
                cc.setNumero(rs.getString("numero"));
                cc.setCodiceSicurezza(rs.getString("codice_sicurezza"));
                cc.setDataScadenza(YearMonth.of(rs.getInt("anno_scadenza"), rs.getInt("mese_scadenza")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return cc;
    }

    public static Integer salvaCarta(CartaCredito cartaCredito, Integer idUtente) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Integer idCarta = null;
        try {
            connection.setAutoCommit(false);
            idCarta = salvaCartaWithConnection(cartaCredito, connection);
            UtenteDB.updateCarta(idCarta, idUtente, connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            pool.freeConnection(connection);
        }
        return idCarta;
    }

    public static Integer salvaCartaWithConnection(CartaCredito cc, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        Integer id = null;
        try {
            String query = "INSERT INTO cartedicredito (intestatario, numero, codice_sicurezza, mese_scadenza, anno_scadenza) " +
                    "VALUES (?, ?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setString(1, cc.getIntestatario());
            ps.setString(2, cc.getNumero());
            ps.setString(3, cc.getCodiceSicurezza());
            ps.setInt(4, cc.getDataScadenza().getMonth().getValue());
            ps.setInt(5, cc.getDataScadenza().getYear());
            ps.executeUpdate();

            String queryId = "SELECT LAST_INSERT_ID() AS last_id;";
            s = connection.createStatement();
            rs = s.executeQuery(queryId);
            rs.next();
            id = rs.getInt("last_id");
        } catch (SQLIntegrityConstraintViolationException e) {
            String query = "SELECT id FROM cartedicredito WHERE intestatario = ? AND numero = ?  AND codice_sicurezza = ? " +
                    "AND mese_scadenza = ? AND anno_scadenza = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, cc.getIntestatario());
            ps.setString(2, cc.getNumero());
            ps.setString(3, cc.getCodiceSicurezza());
            ps.setInt(4, cc.getDataScadenza().getMonth().getValue());
            ps.setInt(5, cc.getDataScadenza().getYear());
            rs = ps.executeQuery();
            rs.next();
            id = rs.getInt("id");
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
        }
        return id;
    }
}
