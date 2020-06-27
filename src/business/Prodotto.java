package business;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;

public class Prodotto {
    private Integer id;
    private String titolo;
    private InputStream foto;
    private String descrizione;
    private String sku;
    private Integer anno;
    private Integer quantita;
    private BigDecimal costo;
    private Date dataCaricamento;
    public enum Categoria {
        LIBRI,
        ALBUM,
        FILM,
        VIDEOGIOCHI
    }
    private Categoria categoria;
    private Integer stelle;
    private DettagliProdotto dettagliProdotto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Date getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(Date dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Integer getStelle() {
        return stelle;
    }

    public void setStelle(Integer stelle) {
        this.stelle = stelle;
    }

    public DettagliProdotto getDettagliProdotto() {
        return dettagliProdotto;
    }

    public void setDettagliProdotto(DettagliProdotto dettagliProdotto) {
        this.dettagliProdotto = dettagliProdotto;
    }
}
