package business;

public class Film extends DettagliProdotto {

    private String regia;
    private String attori;
    public enum SupportoFilm {
        DVD,
        BLURAY
    }
    private SupportoFilm supporto;
    private String paese;

    public String getRegia() {
        return regia;
    }

    public void setRegia(String regia) {
        this.regia = regia;
    }

    public String getAttori() {
        return attori;
    }

    public void setAttori(String attori) {
        this.attori = attori;
    }

    public SupportoFilm getSupporto() {
        return supporto;
    }

    public void setSupporto(SupportoFilm supporto) {
        this.supporto = supporto;
    }

    public String getPaese() {
        return paese;
    }

    public void setPaese(String paese) {
        this.paese = paese;
    }
}
