package data;

import business.Indirizzo;
import business.Utente;

import java.sql.*;
import java.util.ArrayList;

public class UtenteDB {

    public static Integer accessoUtente(String email, String password) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = null;
        try {
            String query = "SELECT id, (password = SHA2(?, 256)) AS password_matches " +
                    "FROM utenti WHERE email = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, password);
            ps.setString(2, email);
            rs = ps.executeQuery();
            rs.next();
            if (rs.getBoolean("password_matches")) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return id;
    }

    public static void registraUtente(Utente u, Indirizzo i) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            int id_residenza = IndirizzoDB.salvaIndirizzoWithConnection(i, connection);

            String query = "INSERT INTO utenti (email, password, nome, cognome, data_nascita, telefono, residenza) " +
                    "VALUES (?, SHA2(?, 256), ?, ?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNome());
            ps.setString(4, u.getCognome());
            ps.setDate(5, u.getDataNascita());
            ps.setString(6, u.getTelefono());
            ps.setInt(7, id_residenza);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("L'email è già stata utilizzata su questo sito");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static ArrayList<Utente> getUtentiPrenotati(int idProdotto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<Utente> utenti = new ArrayList<>();
        try {
            String query = "SELECT email, nome, cognome FROM utenti " +
                    "JOIN prenotazioni p on utenti.id = p.utente " +
                    "WHERE p.prodotto = " + idProdotto;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Utente u = new Utente();
                u.setEmail(rs.getString("email"));
                u.setNome(rs.getString("nome"));
                u.setCognome(rs.getString("cognome"));
                utenti.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return utenti;
    }

    public static void updateCarta(int idCarta, int idUtente, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        String query = "UPDATE utenti SET carta = ? WHERE id = ?";
        ps = connection.prepareStatement(query);
        ps.setInt(1, idCarta);
        ps.setInt(2, idUtente);
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }
}
