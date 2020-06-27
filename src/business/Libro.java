package business;

public class Libro extends DettagliProdotto {

    private String autori;
    private String editore;
    private boolean isFumetto;
    private Integer numeroPagine;

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public boolean isFumetto() {
        return isFumetto;
    }

    public void setFumetto(boolean fumetto) {
        isFumetto = fumetto;
    }

    public Integer getNumeroPagine() {
        return numeroPagine;
    }

    public void setNumeroPagine(Integer numeroPagine) {
        this.numeroPagine = numeroPagine;
    }
}
