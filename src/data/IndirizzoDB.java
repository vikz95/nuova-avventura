package data;

import business.Indirizzo;

import java.sql.*;

public class IndirizzoDB {

    public static Object getIndirizzo(Integer idIndirizzo) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        Indirizzo i = new Indirizzo();
        try {
            String query = "SELECT * FROM indirizzi WHERE id = " + idIndirizzo;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            i.setId(rs.getInt("id"));
            i.setNome(rs.getString("nome"));
            i.setCognome(rs.getString("cognome"));
            i.setProvincia(rs.getString("provincia"));
            i.setCitta(rs.getString("citta"));
            i.setCAP(rs.getString("CAP"));
            i.setIndirizzo(rs.getString("indirizzo"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return i;
    }

    public static Indirizzo getIndirizzoByUtente(int idUtente) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        Indirizzo i = new Indirizzo();
        try {
            String query = "SELECT * FROM indirizzi JOIN utenti u on indirizzi.id = u.residenza " +
                    "WHERE u.id = " + idUtente;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            i.setId(rs.getInt("id"));
            i.setNome(rs.getString("nome"));
            i.setCognome(rs.getString("cognome"));
            i.setProvincia(rs.getString("provincia"));
            i.setCitta(rs.getString("citta"));
            i.setCAP(rs.getString("CAP"));
            i.setIndirizzo(rs.getString("indirizzo"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return i;
    }

    public static Integer salvaIndirizzo(Indirizzo i) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Integer idIndirizzo = null;
        try {
            connection.setAutoCommit(false);
            idIndirizzo = IndirizzoDB.salvaIndirizzoWithConnection(i, connection);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            pool.freeConnection(connection);
        }
        return idIndirizzo;
    }

    public static Integer salvaIndirizzoWithConnection(Indirizzo i, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        Integer id = null;
        try {
            String query = "INSERT INTO indirizzi (nome, cognome, provincia, citta, CAP, indirizzo) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCognome());
            ps.setString(3, i.getProvincia());
            ps.setString(4, i.getCitta());
            ps.setString(5, i.getCAP());
            ps.setString(6, i.getIndirizzo());
            ps.executeUpdate();

            String queryId = "SELECT LAST_INSERT_ID() AS last_id;";
            s = connection.createStatement();
            rs = s.executeQuery(queryId);
            rs.next();
            id = rs.getInt("last_id");
        } catch (SQLIntegrityConstraintViolationException e) {
            String query = "SELECT id FROM indirizzi WHERE nome = ? AND cognome = ?  AND provincia = ? " +
                    "AND citta = ? AND CAP = ? AND indirizzo = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCognome());
            ps.setString(3, i.getProvincia());
            ps.setString(4, i.getCitta());
            ps.setString(5, i.getCAP());
            ps.setString(6, i.getIndirizzo());
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
