<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Registrati"/>
</jsp:include>

<section id="signup" class="my-5">
    <div class="container">
        <div class="row">
            <div class="col-md-8 mx-auto">
                <c:if test="${SIGNUP_ERROR != null}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${SIGNUP_ERROR}"/>
                    </div>
                </c:if>
                <div class="card p-5">
                    <h4 class="text-center">Registrazione Utente</h4>
                    <hr>
                    <form action="AutenticazioneController" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="SIGNUP">
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="nome">Nome</label>
                                <input type="text" class="form-control" name="nome" id="nome" placeholder="Nome"
                                       maxlength="45" required>
                                <div class="invalid-feedback">Inserisci il tuo nome (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="cognome">Cognome</label>
                                <input type="text" class="form-control" name="cognome" id="cognome" maxlength="45"
                                       required
                                       placeholder="Cognome">
                                <div class="invalid-feedback">Inserisci il tuo cognome (max 45 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" name="email" id="email" maxlength="45" required
                                       placeholder="Email">
                                <div class="invalid-feedback">Inserisci l'email (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="password">Password</label>
                                <input type="password" class="form-control" name="password" id="password" minlength="6"
                                       required
                                       placeholder="Password">
                                <div class="invalid-feedback">Scegli una password (min 6 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="data-nascita">Data di Nascita</label>
                                <input type="date" class="form-control" name="dataNascita" id="data-nascita" required
                                       placeholder="dd/mm/yyyy">
                                <div class="invalid-feedback">Inserisci la tua data di nascita</div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="telefono">Numero di Telefono</label>
                                <input type="tel" class="form-control" name="telefono" id="telefono" maxlength="45"
                                       required
                                       placeholder="Telefono">
                                <div class="invalid-feedback">Inserisci il tuo numero (max 45 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col">
                                <label for="indirizzo">Indirizzo di residenza</label>
                                <input type="text" class="form-control" name="indirizzo" id="indirizzo" maxlength="255"
                                       required
                                       placeholder="Inserisci via e numero civico">
                                <div class="invalid-feedback">Inserisci l'indirizzo completo (max 255 caratteri)</div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="citta">Città</label>
                                <input type="text" class="form-control" name="citta" id="citta" placeholder="Città"
                                       maxlength="45" required>
                                <div class="invalid-feedback">Inserisci la città (max 45 caratteri)</div>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="cap">CAP</label>
                                <input type="number" class="form-control" name="cap" id="cap" min="10000"
                                       max="99999" placeholder="CAP" required>
                                <div class="invalid-feedback">Inserisci il CAP (5 cifre)</div>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="provincia">Provincia</label>
                                <input type="text" class="form-control text-uppercase" name="provincia" id="provincia"
                                       minlength="2" maxlength="2" placeholder="Provincia (XX)" required>
                                <div class="invalid-feedback">Inserisci la provincia</div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-info btn-block mt-3">Registrati</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>