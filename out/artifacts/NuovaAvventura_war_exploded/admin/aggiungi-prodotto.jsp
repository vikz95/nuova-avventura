<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Aggiungi Prodotto"/>
</jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section id="nuovo-prodotto" class="my-5">
    <div class="container">
        <c:if test="${PRODUCT_ERROR != null}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${PRODUCT_ERROR}"/>
            </div>
        </c:if>
        <c:if test="${PRODUCT_SUCCESS != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <c:out value="${PRODUCT_SUCCESS}"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <h2 class="text-center text-primary mb-3">Aggiungi Nuovo Prodotto</h2>
        <form action="<c:url value="/admin/GestioneController" />" method="post" class="needs-validation"
              enctype="multipart/form-data" novalidate>
            <input type="hidden" name="command" value="AGGIUNGI_PRODOTTO">
            <div class="row justify-content-between">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="titolo">Titolo</label>
                        <input type="text" class="form-control" name="titolo" id="titolo" maxlength="45" required>
                        <div class="invalid-feedback">Inserisci il titolo (max 45 caratteri)</div>
                    </div>
                    <div class="form-group">
                        <label for="foto">Foto</label>
                        <input type="file" class="form-control-file" name="foto" id="foto" multiple required>
                        <div class="invalid-feedback">Carica una foto del prodotto</div>
                    </div>
                    <div class="form-group">
                        <label for="descrizione">Descrizione</label>
                        <textarea class="form-control" name="descrizione" id="descrizione" rows="9"
                                  required></textarea>
                        <div class="invalid-feedback">Descrivi il prodotto dando maggiori info al cliente</div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="sku">sku</label>
                        <input type="text" class="form-control text-uppercase" name="sku" id="sku" maxlength="9"
                               minlength="9" required>
                        <div class="invalid-feedback">Dai un identificativo al prodotto (9 caratteri)</div>
                    </div>
                    <div class="form-group">
                        <label for="anno">Anno</label>
                        <input type="number" class="form-control" name="anno" id="anno" min="1000" max="9999"
                               required>
                        <div class="invalid-feedback">Indica l'anno d'uscita del prodotto</div>
                    </div>
                    <div class="form-group">
                        <label for="quantita">Quantità disponibile</label>
                        <input type="number" class="form-control" name="quantita" id="quantita" min="0" required>
                        <div class="invalid-feedback">Indica la giacenza in magazzino del prodotto</div>
                    </div>
                    <div class="form-group">
                        <label for="costo">Prezzo</label>
                        <input type="number" class="form-control" name="costo" id="costo" min="0" max="9999.99"
                               step=".01" required>
                        <div class="invalid-feedback">Specifica il costo in euro (max 9999.99)</div>
                    </div>
                    <div class="form-group">
                        <label for="categoria">Categoria</label>
                        <select name="categoria" id="categoria" class="form-control" required>
                            <option value="" selected disabled>Scegli una categoria...</option>
                            <option value="LIBRI">Libro</option>
                            <option value="ALBUM">Album musicale</option>
                            <option value="FILM">Film</option>
                            <option value="VIDEOGIOCHI">Videogioco</option>
                        </select>
                        <div class="invalid-feedback">Scegli la categoria a cui il prodotto appartiene</div>
                    </div>
                </div>
            </div>
            <div id="dettagli-categoria" class="mt-3">

            </div>
            <input type="submit" class="btn btn-success btn-block col-md-5 mt-3 mx-auto" value="Aggiungi Prodotto">
        </form>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
<script src="${pageContext.request.contextPath}/js/prodotto.js"></script>
</body>
</html>