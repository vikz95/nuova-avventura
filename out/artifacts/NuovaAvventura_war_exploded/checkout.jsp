<%@ page import="business.Indirizzo" %>
<%@ page import="business.TipologiaSpedizione" %>
<%@ page import="business.CartaCredito" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Checkout"/>
</jsp:include>

<%
    Indirizzo indirizzo = (Indirizzo) request.getAttribute("indirizzo");
    pageContext.setAttribute("indirizzo", indirizzo);
    TipologiaSpedizione posta = (TipologiaSpedizione) request.getAttribute("posta");
    pageContext.setAttribute("posta", posta);
    TipologiaSpedizione corriere = (TipologiaSpedizione) request.getAttribute("corriere");
    pageContext.setAttribute("corriere", corriere);
    CartaCredito carta = (CartaCredito) request.getAttribute("carta");
    pageContext.setAttribute("carta", carta);
%>

<section id="checkout">
    <div class="container">
        <form action="AcquistoController" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="command" value="EFFETTUA_ACQUISTO">
            <input type="hidden" name="spedizione" id="spedizione" value="posta">
            <div class="row my-5 justify-content-between">
                <div class="col-md-8 bg-white py-3 pl-5">
                    <div class="indirizzo">
                        <h4>Indirizzo di spedizione</h4>
                        <hr>
                        <div class="form-row">
                            <div class="form-group col-md-5">
                                <label for="nome">Nome</label>
                                <input type="text" name="nome" id="nome" value="${indirizzo.nome}" class="form-control"
                                       maxlength="45" required>
                                <div class="invalid-feedback">Inserisci il nome (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-5">
                                <label for="cognome">Cognome</label>
                                <input type="text" name="cognome" id="cognome" value="${indirizzo.cognome}"
                                       class="form-control" maxlength="45" required>
                                <div class="invalid-feedback">Inserisci il cognome (max 45 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-10">
                                <label for="indirizzo">Indirizzo</label>
                                <input type="text" name="indirizzo" id="indirizzo" value="${indirizzo.indirizzo}"
                                       class="form-control" maxlength="255" required>
                                <div class="invalid-feedback">Inserisci l'indirizzo completo (max 255 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="citta">Città</label>
                                <input type="text" class="form-control" name="citta" id="citta"
                                       value="${indirizzo.citta}" maxlength="45" required>
                                <div class="invalid-feedback">Inserisci la città (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="cap">CAP</label>
                                <input type="number" class="form-control" name="cap" id="cap" value="${indirizzo.CAP}"
                                       min="10000" max="99999" required>
                                <div class="invalid-feedback">Inserisci il CAP (5 cifre)</div>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="provincia">Provincia</label>
                                <input type="text" class="form-control" name="provincia" id="provincia"
                                       value="${indirizzo.provincia}" minlength="2" maxlength="2" required>
                                <div class="invalid-feedback">Inserisci la provincia (XX)</div>
                            </div>
                        </div>
                    </div>
                    <div class="tipo-spedizione mt-3">
                        <h4>Tipo di spedizione</h4>
                        <hr>
                        <div class="form-row">
                            <div class="form-group col-md-5">
                                <input type="radio" name="tipoSpedizione" id="posta" value="${posta.tipo}"
                                       class="card-input-element" checked required/>
                                <div class="card card-input" onclick="postaSelezionato()">
                                    <div class="card-header">Spedizione Postale</div>
                                    <div class="card-body">
                                        <h3 class="text-right">${posta.costo}€</h3>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-5">
                                <input type="radio" name="tipoSpedizione" id="corriere" value="${posta.tipo}"
                                       class="card-input-element" required/>
                                <div class="card card-input" onclick="corriereSelezionato()">
                                    <div class="card-header">Corriere Espresso</div>
                                    <div class="card-body">
                                        <h3 class="text-right">${corriere.costo}€</h3>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="carta-credito mt-3">
                        <h4>Carta di credito</h4>
                        <hr>
                        <div class="form-row">
                            <div class="form-group col-md-5">
                                <label for="intestatario">Intestatario</label>
                                <input type="text" name="intestatario" id="intestatario" value="${carta.intestatario}"
                                       class="form-control" maxlength="45" required>
                                <div class="invalid-feedback">Inserisci l'intestatario (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-5">
                                <label for="numero-carta">Numero</label>
                                <input type="text" name="numeroCarta" id="numero-carta" value="${carta.numero}"
                                       class="form-control" pattern="\d{13}|\d{16}" required>
                                <div class="invalid-feedback">Inserisci il numero della carta (13 o 16 cifre)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-5">
                                <label for="scadenza">Data scadenza</label>
                                <input type="month" name="scadenza" id="scadenza" class="form-control"
                                       value="${carta.dataScadenza}" required>
                                <div class="invalid-feedback">Inserisci la data di scadenza</div>
                            </div>
                            <div class="form-group col-md-5">
                                <label for="codice-sicurezza">Codice di sicurezza</label>
                                <input type="number" name="codiceSicurezza" id="codice-sicurezza" class="form-control"
                                       value="${carta.codiceSicurezza}" min="100" max="9999" required>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <h3>Riepilogo ordine</h3>
                    <hr>
                    <div class="d-flex mb-3 justify-content-between">
                        <span>Subtotale</span>
                        <span>${param.totaleParziale}€</span>
                    </div>
                    <div class="d-flex mb-3 justify-content-between">
                        <span>Spese di spedizione</span>
                        <span id="displaySpeseSpedizione">${posta.costo}€</span>
                    </div>
                    <div class="d-flex mb-3 justify-content-between">
                        <h4>TOTALE</h4>
                        <h4 id="displayTotale">${param.totaleParziale + posta.costo}€</h4>
                    </div>
                    <button type="submit" class="btn btn-info btn-block btn-lg text-white">Acquista ora</button>
                </div>
            </div>
        </form>
    </div>
</section>

<script>
    function postaSelezionato() {
        let speseSpedizione = ${posta.costo};
        let totaleParziale = ${param.totaleParziale};
        let totale = speseSpedizione + totaleParziale;
        document.getElementById('displaySpeseSpedizione').innerText = speseSpedizione.toFixed(2) + '€';
        document.getElementById('displayTotale').innerText = totale.toFixed(2) + '€';
        document.getElementById('corriere').checked = false;
        document.getElementById('posta').checked = true;
        document.getElementById('spedizione').value = 'posta';
    }

    function corriereSelezionato() {
        let speseSpedizione = ${corriere.costo};
        let totaleParziale = ${param.totaleParziale};
        let totale = speseSpedizione + totaleParziale;
        document.getElementById('displaySpeseSpedizione').innerText = speseSpedizione.toFixed(2) + '€';
        document.getElementById('displayTotale').innerText = totale.toFixed(2) + '€';
        document.getElementById('posta').checked = false;
        document.getElementById('corriere').checked = true;
        document.getElementById('spedizione').value = 'corriere';
    }

    year = new Date().getFullYear();
    month = new Date().getMonth() + 1;
    monthS = month < 10 ? '0' + month : month;
    document.getElementById('scadenza').setAttribute('min', year + '-' + monthS);
</script>

<jsp:include page="footer.jsp"/>
