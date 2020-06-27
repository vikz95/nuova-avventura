package business;

public class Videogioco extends DettagliProdotto {

    private String produttore;
    private String piattaforma;
    private Integer etaConsigliata;

    public String getProduttore() {
        return produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    public String getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(String piattaforma) {
        this.piattaforma = piattaforma;
    }

    public Integer getEtaConsigliata() {
        return etaConsigliata;
    }

    public void setEtaConsigliata(Integer etaConsigliata) {
        this.etaConsigliata = etaConsigliata;
    }
}
