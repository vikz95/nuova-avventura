package data;

import business.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ProdottoDB {

    public static void aggiungiProdotto(Prodotto p) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO prodotti (sku, foto, quantita, titolo, descrizione, anno, costo, data_caricamento, categoria) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setString(1, p.getSku().toUpperCase());
            ps.setBlob(2, p.getFoto());
            ps.setInt(3, p.getQuantita());
            ps.setString(4, p.getTitolo());
            ps.setString(5, p.getDescrizione());
            ps.setInt(6, p.getAnno());
            ps.setBigDecimal(7, p.getCosto());
            ps.setDate(8, p.getDataCaricamento());
            ps.setString(9, p.getCategoria().name());
            ps.executeUpdate();

            String queryId = "SELECT LAST_INSERT_ID() AS last_id;";
            s = connection.createStatement();
            rs = s.executeQuery(queryId);
            rs.next();
            int id = rs.getInt("last_id");

            switch (p.getCategoria()) {
                case LIBRI:
                    aggiungiLibro(id, (Libro) p.getDettagliProdotto(), connection);
                    break;
                case ALBUM:
                    aggiungiAlbum(id, (Album) p.getDettagliProdotto(), connection);
                    break;
                case FILM:
                    aggiungiFilm(id, (Film) p.getDettagliProdotto(), connection);
                    break;
                case VIDEOGIOCHI:
                    aggiungiVideogioco(id, (Videogioco) p.getDettagliProdotto(), connection);
                    break;
            }
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

    private static void aggiungiLibro(int id, Libro l, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "INSERT INTO libri(id_libro, autori, editore, fumetto, pagine) " +
                "VALUES (?, ?, ?, ?, ?);";
        ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, l.getAutori());
        ps.setString(3, l.getEditore());
        ps.setBoolean(4, l.isFumetto());
        ps.setInt(5, l.getNumeroPagine());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static void aggiungiAlbum(int id, Album a, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "INSERT INTO album(id_album, autori, etichetta, supporto, numero_supporti) " +
                "VALUES (?, ?, ?, ?, ?)";
        ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, a.getAutori());
        ps.setString(3, a.getEtichetta());
        ps.setString(4, a.getSupporto().name());
        ps.setInt(5, a.getNumeroSupporti());
        ps.executeUpdate();
        aggiungiCanzoni(id, a.getCanzoni(), connection);
        DBUtil.closePreparedStatement(ps);
    }

    private static void aggiungiCanzoni(int id, ArrayList<Canzone> canzoni, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        for (Canzone c : canzoni) {
            String query = "INSERT INTO canzoni(posizione, titolo, durata, album_id) " +
                    "VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, c.getPosizione());
            ps.setString(2, c.getTitolo());
            ps.setTime(3, c.getDurata());
            ps.setInt(4, id);
            ps.executeUpdate();
        }
        DBUtil.closePreparedStatement(ps);
    }

    private static void aggiungiFilm(int id, Film f, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "INSERT INTO film(id_film, regia, paese, supporto, attori) " +
                "VALUES (?, ?, ?, ?, ?)";
        ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, f.getRegia());
        ps.setString(3, f.getPaese());
        ps.setString(4, f.getSupporto().name());
        ps.setString(5, f.getAttori());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static void aggiungiVideogioco(int id, Videogioco v, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "INSERT INTO videogiochi(id_videogioco, produttore, piattaforma, eta_consigliata) " +
                "VALUES (?, ?, ?, ?)";
        ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, v.getProduttore());
        ps.setString(3, v.getPiattaforma());
        ps.setInt(4, v.getEtaConsigliata());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static Prodotto estraiProdotto(Connection connection, ResultSet rs) throws SQLException {
        Prodotto p = new Prodotto();
        p.setId(rs.getInt("id"));
        p.setSku(rs.getString("sku"));
        p.setFoto(rs.getBinaryStream("foto"));
        p.setQuantita(rs.getInt("quantita"));
        p.setTitolo(rs.getString("titolo"));
        p.setDescrizione(rs.getString("descrizione"));
        p.setAnno(rs.getInt("anno"));
        p.setCosto(rs.getBigDecimal("costo"));
        p.setDataCaricamento(rs.getDate("data_caricamento"));
        p.setCategoria(Prodotto.Categoria.valueOf(rs.getString("categoria")));
        p.setStelle((int) Math.round(rs.getDouble("media_recensioni")));
        switch (p.getCategoria()) {
            case LIBRI:
                p.setDettagliProdotto(getLibro(p.getId(), connection));
                break;
            case ALBUM:
                p.setDettagliProdotto(getAlbum(p.getId(), connection));
                break;
            case FILM:
                p.setDettagliProdotto(getFilm(p.getId(), connection));
                break;
            case VIDEOGIOCHI:
                p.setDettagliProdotto(getVideogioco(p.getId(), connection));
                break;
        }
        return p;
    }

    public static Prodotto getProdotto(int id) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        ResultSet rs = null;
        Prodotto p = null;
        try {
            connection.setAutoCommit(false);
            String query = "SELECT * FROM prodotti WHERE id = " + id;
            s = connection.createStatement();
            rs = s.executeQuery(query);
            rs.next();
            p = estraiProdotto(connection, rs);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return p;
    }

    public static ArrayList<Prodotto> getProdottiByTerminiDiRicerca(String termini) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query = "SELECT *, MATCH (titolo, descrizione) AGAINST (?) AS rilevanza " +
                    "FROM prodotti " +
                    "WHERE MATCH (titolo, descrizione) AGAINST (?) " +
                    "ORDER BY rilevanza DESC";
            ps = connection.prepareStatement(query);
            ps.setString(1, termini);
            ps.setString(2, termini);
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    public static ArrayList<Prodotto> getProdottiByTerminiDiRicercaAndCategoria(String termini, String categoria) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query = "SELECT *, MATCH (titolo, descrizione) AGAINST (?) AS rilevanza " +
                    "FROM prodotti " +
                    "WHERE categoria = ? AND MATCH (titolo, descrizione) AGAINST (?) " +
                    "ORDER BY rilevanza DESC";
            ps = connection.prepareStatement(query);
            ps.setString(1, termini);
            ps.setString(2, categoria);
            ps.setString(3, termini);
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    public static ArrayList<Prodotto> getProdottiByCategoria(String categoria) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query = "SELECT * FROM prodotti WHERE categoria = ? ORDER BY data_caricamento DESC";
            ps = connection.prepareStatement(query);
            ps.setString(1, categoria);
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    public static ArrayList<Prodotto> getProdottiByCategoriaAndLimit(String categoria, int limit) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query = "SELECT * FROM prodotti WHERE categoria = ? ORDER BY data_caricamento DESC LIMIT " + limit;
            ps = connection.prepareStatement(query);
            ps.setString(1, categoria);
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    public static ArrayList<Prodotto> getProdottiByCategoriaAndTipo(String categoria, String tipo) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query;
            if (categoria.equals("LIBRI")) {
                query = "SELECT * FROM prodotti AS p " +
                        "INNER JOIN libri AS l ON p.id = l.id_libro " +
                        "WHERE p.categoria = ? AND l.fumetto = true " +
                        "ORDER BY data_caricamento DESC";
            } else if (categoria.equals("ALBUM")){
                query = "SELECT * FROM prodotti AS p " +
                        "INNER JOIN album AS a ON p.id = a.id_album " +
                        "WHERE p.categoria = ? AND a.supporto = ? " +
                        "ORDER BY data_caricamento DESC";
            } else {
                query = "SELECT * FROM prodotti AS p " +
                        "INNER JOIN film AS f ON p.id = f.id_film " +
                        "WHERE p.categoria = ? AND f.supporto = ? " +
                        "ORDER BY data_caricamento DESC";
            }
            ps = connection.prepareStatement(query);
            ps.setString(1, categoria);
            if (!categoria.equals("LIBRI")) {
                ps.setString(2, tipo);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    public static ArrayList<Prodotto> getProdottiByCategoriaAndOrdinamento(String categoria, String ordinamento) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            String query = "SELECT * FROM prodotti WHERE categoria = ? ORDER BY " + ordinamento;
            ps = connection.prepareStatement(query);
            ps.setString(1, categoria);
            rs = ps.executeQuery();
            while (rs.next()) {
                prodotti.add(estraiProdotto(connection, rs));
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            DBUtil.closeResultSet(rs);
            pool.freeConnection(connection);
        }
        return prodotti;
    }

    private static Libro getLibro(int id, Connection connection) throws SQLException {
        Statement s;
        ResultSet rs;
        String query = "SELECT * FROM libri WHERE id_libro = " + id;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        rs.next();
        Libro l = new Libro();
        l.setAutori(rs.getString("autori"));
        l.setEditore(rs.getString("editore"));
        l.setFumetto(rs.getBoolean("fumetto"));
        l.setNumeroPagine(rs.getInt("pagine"));
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return l;
    }

    private static Album getAlbum(int id, Connection connection) throws SQLException {
        Statement s;
        ResultSet rs;
        String query = "SELECT * FROM album WHERE id_album = " + id;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        rs.next();
        Album a = new Album();
        a.setAutori(rs.getString("autori"));
        a.setEtichetta(rs.getString("etichetta"));
        a.setSupporto(Album.SupportoAlbum.valueOf(rs.getString("supporto")));
        a.setNumeroSupporti(rs.getInt("numero_supporti"));
        a.setCanzoni(getCanzoni(id, connection));
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return a;
    }

    private static ArrayList<Canzone> getCanzoni(int id, Connection connection) throws SQLException {
        Statement s;
        ResultSet rs;
        ArrayList<Canzone> canzoni = new ArrayList<>();
        String query = "SELECT * FROM canzoni WHERE album_id = " + id;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        while (rs.next()) {
            Canzone c = new Canzone();
            c.setId(rs.getInt("id"));
            c.setPosizione(rs.getInt("posizione"));
            c.setTitolo(rs.getString("titolo"));
            c.setDurata(rs.getTime("durata"));
            canzoni.add(c);
        }
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return canzoni;
    }

    private static Film getFilm(int id, Connection connection) throws SQLException {
        Statement s;
        ResultSet rs;
        String query = "SELECT * FROM film WHERE id_film = " + id;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        rs.next();
        Film f = new Film();
        f.setRegia(rs.getString("regia"));
        f.setPaese(rs.getString("paese"));
        f.setSupporto(Film.SupportoFilm.valueOf(rs.getString("supporto")));
        f.setAttori(rs.getString("attori"));
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return f;
    }

    private static Videogioco getVideogioco(int id, Connection connection) throws SQLException {
        Statement s;
        ResultSet rs;
        String query = "SELECT * FROM videogiochi WHERE id_videogioco = " + id;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        rs.next();
        Videogioco v = new Videogioco();
        v.setProduttore(rs.getString("produttore"));
        v.setPiattaforma(rs.getString("piattaforma"));
        v.setEtaConsigliata(rs.getInt("eta_consigliata"));
        DBUtil.closeStatement(s);
        DBUtil.closeResultSet(rs);
        return v;
    }

    public static void cancellaProdotto(int id) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement s = null;
        try {
            String query = "DELETE FROM prodotti WHERE id = " + id;
            s = connection.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closeStatement(s);
            pool.freeConnection(connection);
        }
    }

    public static void updateProdotto(Prodotto p) throws SQLIntegrityConstraintViolationException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            String query = "UPDATE prodotti SET sku = ?, quantita = quantita + ?, titolo = ?, descrizione = ?, anno = ?, costo = ?," +
                    "data_caricamento = ?, categoria = ? WHERE id = " + p.getId();
            ps = connection.prepareStatement(query);
            ps.setString(1, p.getSku().toUpperCase());
            ps.setInt(2, p.getQuantita());
            ps.setString(3, p.getTitolo());
            ps.setString(4, p.getDescrizione());
            ps.setInt(5, p.getAnno());
            ps.setBigDecimal(6, p.getCosto());
            ps.setDate(7, p.getDataCaricamento());
            ps.setString(8, p.getCategoria().name());
            ps.executeUpdate();
            if (p.getFoto().available() != 0) {
                query = "UPDATE prodotti SET foto = ? WHERE id = " + p.getId();
                ps = connection.prepareStatement(query);
                ps.setBlob(1, p.getFoto());
                ps.executeUpdate();
            }
            switch (p.getCategoria()) {
                case LIBRI:
                    updateLibro(p.getId(), (Libro) p.getDettagliProdotto(), connection);
                    break;
                case ALBUM:
                    updateAlbum(p.getId(), (Album) p.getDettagliProdotto(), connection);
                    break;
                case FILM:
                    updateFilm(p.getId(), (Film) p.getDettagliProdotto(), connection);
                    break;
                case VIDEOGIOCHI:
                    updateVideogioco(p.getId(), (Videogioco) p.getDettagliProdotto(), connection);
                    break;
            }
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    private static void updateLibro(int id, Libro libro, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "UPDATE libri SET autori = ?, editore = ?, fumetto = ? ,pagine = ? WHERE id_libro = " + id;
        ps = connection.prepareStatement(query);
        ps.setString(1, libro.getAutori());
        ps.setString(2, libro.getEditore());
        ps.setBoolean(3, libro.isFumetto());
        ps.setInt(4, libro.getNumeroPagine());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static void updateAlbum(int id, Album album, Connection connection) throws SQLException {
        PreparedStatement ps;
        Statement s;
        String query = "UPDATE album SET autori = ?, etichetta = ?, supporto = ?, numero_supporti = ? WHERE id_album = " + id;
        ps = connection.prepareStatement(query);
        ps.setString(1, album.getAutori());
        ps.setString(2, album.getEtichetta());
        ps.setString(3, album.getSupporto().name());
        ps.setInt(4, album.getNumeroSupporti());
        ps.executeUpdate();
        query = "DELETE FROM canzoni WHERE album_id = " + id;
        s = connection.createStatement();
        s.executeUpdate(query);
        aggiungiCanzoni(id, album.getCanzoni(), connection);
        DBUtil.closePreparedStatement(ps);
        DBUtil.closeStatement(s);
    }

    private static void updateFilm(int id, Film film, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "UPDATE film SET attori = ?, regia = ?, supporto = ?, paese = ? WHERE id_film = " + id;
        ps = connection.prepareStatement(query);
        ps.setString(1, film.getAttori());
        ps.setString(2, film.getRegia());
        ps.setString(3, film.getSupporto().name());
        ps.setString(4, film.getPaese());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }

    private static void updateVideogioco(int id, Videogioco videogioco, Connection connection) throws SQLException {
        PreparedStatement ps;
        String query = "UPDATE videogiochi SET produttore = ?, piattaforma = ?, eta_consigliata = ? " +
                "WHERE id_videogioco = " + id;
        ps = connection.prepareStatement(query);
        ps.setString(1, videogioco.getProduttore());
        ps.setString(2, videogioco.getPiattaforma());
        ps.setInt(3, videogioco.getEtaConsigliata());
        ps.executeUpdate();
        DBUtil.closePreparedStatement(ps);
    }
}
