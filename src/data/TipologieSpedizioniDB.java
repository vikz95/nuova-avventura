package data;

import business.TipologiaSpedizione;

import java.sql.*;

public class TipologieSpedizioniDB {

    public static TipologiaSpedizione getSpedizione(int id) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        TipologiaSpedizione ts = new TipologiaSpedizione();

        try {
            String query = "SELECT * FROM tipologiespedizioni WHERE id = " + id;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            ts.setId(rs.getInt("id"));
            ts.setTipo(rs.getString("tipo"));
            ts.setCosto(rs.getBigDecimal("costo"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return ts;
    }

    public static TipologiaSpedizione getSpedizione(String tipo) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipologiaSpedizione ts = null;

        try {
            String query = "SELECT * FROM tipologiespedizioni WHERE tipo = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, tipo);
            rs = ps.executeQuery();
            rs.next();

            ts = new TipologiaSpedizione();
            ts.setId(rs.getInt("id"));
            ts.setTipo(rs.getString("tipo"));
            ts.setCosto(rs.getBigDecimal("costo"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return ts;
    }

    public static void updateCosto(TipologiaSpedizione ts) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        try {
            String query = "UPDATE tipologiespedizioni SET costo = ? WHERE tipo = ?";
            ps = connection.prepareStatement(query);
            ps.setBigDecimal(1, ts.getCosto());
            ps.setString(2, ts.getTipo());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
}
