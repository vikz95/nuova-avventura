package data;

import business.Ordine;
import business.Pagamento;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class PagamentoDB {

    public static Pagamento getPagamento(Integer idPagamento) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        Pagamento p = new Pagamento();
        try {
            String query = "SELECT * FROM pagamenti WHERE id = " + idPagamento;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            p.setId(rs.getInt("id"));
            p.setIdCarta(rs.getInt("carta"));
            p.setStato(Pagamento.StatoPagamento.valueOf(rs.getString("stato")));
            p.setData(rs.getDate("data"));
            p.setImporto(rs.getBigDecimal("importo"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return p;
    }

    public static ArrayList<Pagamento> getPagamenti() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        ArrayList<Pagamento> pagamenti = new ArrayList<>();
        try {
            String query = "SELECT * FROM pagamenti ORDER BY data DESC, id DESC";
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Pagamento p = new Pagamento();
                p.setId(rs.getInt("id"));
                p.setIdCarta(rs.getInt("carta"));
                p.setStato(Pagamento.StatoPagamento.valueOf(rs.getString("stato")));
                p.setData(rs.getDate("data"));
                p.setImporto(rs.getBigDecimal("importo"));
                pagamenti.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return pagamenti;
    }

    public static int creaPagamento(int idCarta, Connection connection) throws SQLException {
        String query = "INSERT INTO pagamenti (carta, stato) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, idCarta);
        ps.setString(2, Pagamento.StatoPagamento.IN_ATTESA.name());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);

        String queryId = "SELECT LAST_INSERT_ID() AS last_id;";
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(queryId);
        rs.next();
        int id = rs.getInt("last_id");
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return id;
    }

    public static void setImporto(int idPagamento, BigDecimal importo, Connection connection) throws SQLException {
        String query = "UPDATE pagamenti SET importo = ? WHERE id = " + idPagamento;
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setBigDecimal(1, importo);
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    public static void setStato(int idPagamento, int idOrdine, Pagamento.StatoPagamento stato) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            String query = "UPDATE pagamenti SET stato = ?, data = ? WHERE id = " + idPagamento;
            ps = connection.prepareStatement(query);
            ps.setString(1, stato.name());
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.executeUpdate();

            if (stato == Pagamento.StatoPagamento.ACCETTATO) {
                OrdineDB.setStato(idOrdine, Ordine.StatoOrdine.PRONTO, connection);
            } else {
                OrdineDB.setStato(idOrdine, Ordine.StatoOrdine.ANNULLATO, connection);
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
}
