package business;

import java.sql.Date;

public class Utente {

    private Integer id;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String telefono;

    private Indirizzo residenza;
    private CartaCredito carta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Indirizzo getResidenza() {
        return residenza;
    }

    public void setResidenza(Indirizzo residenza) {
        this.residenza = residenza;
    }

    public CartaCredito getCarta() {
        return carta;
    }

    public void setCarta(CartaCredito carta) {
        this.carta = carta;
    }
}
