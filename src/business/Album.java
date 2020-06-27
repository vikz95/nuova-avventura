package business;

import java.util.ArrayList;

public class Album extends DettagliProdotto {

    private String autori;
    private String etichetta;
    public enum SupportoAlbum {
        CD,
        VINILE;
    }
    private SupportoAlbum supporto;
    private Integer numeroSupporti;
    private ArrayList<Canzone> canzoni = new ArrayList<>();

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getEtichetta() {
        return etichetta;
    }

    public void setEtichetta(String etichetta) {
        this.etichetta = etichetta;
    }

    public SupportoAlbum getSupporto() {
        return supporto;
    }

    public void setSupporto(SupportoAlbum supporto) {
        this.supporto = supporto;
    }

    public Integer getNumeroSupporti() {
        return numeroSupporti;
    }

    public void setNumeroSupporti(Integer numeroSupporti) {
        this.numeroSupporti = numeroSupporti;
    }

    public void aggiungiCanzone(Canzone c) {
        canzoni.add(c);
    }

    public ArrayList<Canzone> getCanzoni() {
        return canzoni;
    }

    public void setCanzoni(ArrayList<Canzone> canzoni) {
        this.canzoni = canzoni;
    }
}
