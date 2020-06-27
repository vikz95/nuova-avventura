package business;

import java.time.YearMonth;

public class CartaCredito {

    private Integer id;
    private String intestatario;
    private String numero;
    private String codiceSicurezza;
    private YearMonth dataScadenza;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodiceSicurezza() {
        return codiceSicurezza;
    }

    public void setCodiceSicurezza(String codiceSicurezza) {
        this.codiceSicurezza = codiceSicurezza;
    }

    public YearMonth getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(YearMonth dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}
