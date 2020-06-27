package business;

import java.math.BigDecimal;
import java.sql.Date;

public class Pagamento {

    private Integer id;
    private Integer idCarta;
    public enum StatoPagamento {
        IN_ATTESA,
        ACCETTATO,
        DATI_SCORRETTI,
        SUPERAMENTO_TETTO,
        CREDITO_INSUFFICIENTE
    }
    private StatoPagamento stato;
    private Date data;
    private BigDecimal importo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(Integer idCarta) {
        this.idCarta = idCarta;
    }

    public StatoPagamento getStato() {
        return stato;
    }

    public void setStato(StatoPagamento stato) {
        this.stato = stato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }
}
