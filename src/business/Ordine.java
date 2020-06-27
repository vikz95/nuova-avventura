package business;

import java.sql.Date;

public class Ordine {

    private Integer id;
    private Date dataCreazione;
    private Date dataSpedizione;
    public enum StatoOrdine {
        IN_CORSO,
        PRONTO,
        SPEDITO,
        ANNULLATO
    }
    private StatoOrdine stato;
    private Integer idUtente;
    private Integer idIndirizzo;
    private Integer idSpedizione;
    private Integer idPagamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public Integer getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(Integer idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public Integer getIdSpedizione() {
        return idSpedizione;
    }

    public void setIdSpedizione(Integer idSpedizione) {
        this.idSpedizione = idSpedizione;
    }

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }
}
