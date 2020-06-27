<%@ page import="business.*" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Modifica Prodotto"/>
</jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Prodotto p = (Prodotto) request.getAttribute("prodotto");
    DettagliProdotto dp = p.getDettagliProdotto();
    pageContext.setAttribute("p", p);
    pageContext.setAttribute("dp", dp);
%>

<section id="nuovo-prodotto" class="my-5">
    <div class="container">
        <c:if test="${PRODUCT_ERROR != null}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${PRODUCT_ERROR}"/>
            </div>
        </c:if>
        <h2 class="text-center text-primary mb-3">Modifica Prodotto</h2>
        <form action="<c:url value="/admin/GestioneController" />" method="post" class="needs-validation"
              enctype="multipart/form-data" novalidate>
            <input type="hidden" name="command" value="MODIFICA_PRODOTTO">
            <input type="hidden" name="id" value="${p.id}">
            <div class="row justify-content-between">
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="titolo">Titolo</label>
                        <input type="text" class="form-control" name="titolo" id="titolo" value="${p.titolo}"
                               maxlength="45" required>
                        <div class="invalid-feedback">Inserisci il titolo (max 45 caratteri)</div>
                    </div>
                    <div class="form-group">
                        <label for="foto">Foto</label>
                        <input type="file" class="form-control-file" name="foto" id="foto">
                        <div class="invalid-feedback">Carica una foto del prodotto</div>
                    </div>
                    <div class="form-group">
                        <label for="descrizione">Descrizione</label>
                        <textarea class="form-control" name="descrizione" id="descrizione" rows="9"
                                  required>${p.descrizione}</textarea>
                        <div class="invalid-feedback">Descrivi il prodotto dando maggiori info al cliente</div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="form-group">
                        <label for="sku">sku</label>
                        <input type="text" class="form-control text-uppercase" name="sku" id="sku" value="${p.sku}"
                               maxlength="9"
                               minlength="9" required>
                        <div class="invalid-feedback">Dai un identificativo al prodotto (9 caratteri)</div>
                    </div>
                    <div class="form-group">
                        <label for="anno">Anno</label>
                        <input type="number" class="form-control" name="anno" id="anno" value="${p.anno}" min="1000"
                               max="9999"
                               required>
                        <div class="invalid-feedback">Indica l'anno d'uscita del prodotto</div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="vecchiaQuantita">Quantità disponibile</label>
                            <input type="number" class="form-control" name="vecchiaQuantita" id="vecchiaQuantita"
                                   value="${p.quantita}" readonly>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="quantita">Aumenta/Riduci quantità di</label>
                            <input type="number" class="form-control" name="quantita" id="quantita" min="-${p.quantita}" value="0" required>
                            <div class="invalid-feedback">Indica di quanto aumentare o ridurre la quantità</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="costo">Prezzo</label>
                        <input type="number" class="form-control" name="costo" id="costo" value="${p.costo}" min="0"
                               max="9999.99"
                               step=".01" required>
                        <div class="invalid-feedback">Specifica il costo in euro (max 9999.99)</div>
                    </div>
                    <div class="form-group">
                        <label for="categoria">Categoria</label>
                        <select name="categoria" id="categoria" class="form-control" readonly="">
                            <c:choose>
                                <c:when test="${p.categoria.name() == 'LIBRI'}">
                                    <option value="LIBRI" selected>Libro</option>
                                </c:when>
                                <c:when test="${p.categoria.name() == 'ALBUM'}">
                                    <option value="ALBUM" selected>Album musicale</option>
                                </c:when>
                                <c:when test="${p.categoria.name() == 'FILM'}">
                                    <option value="FILM" selected>Film</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="VIDEOGIOCHI" selected>Videogioco</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
            </div>
            <div id="dettagli-categoria" class="mt-3">
                <c:choose>
                    <c:when test="${p.categoria.name() == 'LIBRI'}">
                        <%
                            Libro l = (Libro) dp;
                            pageContext.setAttribute("l", l);
                        %>
                        <div class="row justify-content-center">
                            <div class="col-md-5">
                                <h3>Dettagli Libro</h3>
                                <div class="form-group">
                                    <label for="autori">Autori</label>
                                    <input type="text" class="form-control" name="autori" id="autori"
                                           value="${l.autori}" maxlength="255" required>
                                    <div class="invalid-feedback">Inserisci i nomi degli autori separati da virgola (max
                                        255 caratteri)
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="editore">Editore</label>
                                    <input type="text" class="form-control" name="editore" id="editore"
                                           value="${l.editore}" maxlength="45"
                                           required>
                                    <div class="invalid-feedback">Inserisci l'editore (max 45 caratteri)</div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-6">
                                        <div class="form-check-label mb-2">È un fumetto?</div>
                                        <label class="mr-5">
                                            <input type="radio" class="form-check form-check-inline" name="fumetto"
                                                   value="si" ${l.fumetto ? 'checked' : ''}>sì
                                        </label>
                                        <label>
                                            <input type="radio" class="form-check form-check-inline" name="fumetto"
                                                   value="no" ${l.fumetto ? '' : 'checked'}>no
                                        </label>
                                    </div>
                                    <div class="form-group col-6">
                                        <label for="pagine">Numero Pagine</label>
                                        <input type="number" class="form-control" name="pagine" id="pagine"
                                               value="${l.numeroPagine}" required>
                                        <div class="invalid-feedback">Inserisci il numero di pagine del libro</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${p.categoria.name() == 'ALBUM'}">
                        <%
                            Album a = (Album) dp;
                            pageContext.setAttribute("a", a);
                        %>
                        <div class="row justify-content-center">
                            <div class="col-md-5">
                                <h3>Dettagli Album</h3>
                                <div class="form-group">
                                    <label for="autori2">Autori</label>
                                    <input type="text" class="form-control" name="autori" id="autori2"
                                           value="${a.autori}"
                                           maxlength="255" required>
                                    <div class="invalid-feedback">Inserisci gli autori separati da virgola (max 255
                                        caratteri)
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="etichetta">Etichetta</label>
                                    <input type="text" class="form-control" name="etichetta" id="etichetta"
                                           value="${a.etichetta}"
                                           maxlength="45" required>
                                    <div class="invalid-feedback">Inserisci l'etichetta discografica</div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-6">
                                        <div class="form-check-label mb-2">Tipo di supporto</div>
                                        <label class="mr-5">
                                            <input type="radio" class="form-check form-check-inline" name="supporto"
                                                   value="CD" ${a.supporto.name() == 'CD' ? 'checked' : ''}>CD
                                        </label>
                                        <label>
                                            <input type="radio" class="form-check form-check-inline" name="supporto"
                                                   value="VINILE" ${a.supporto.name() == 'VINILE' ? 'checked' : ''}>Vinile
                                        </label>
                                    </div>
                                    <div class="form-group col-6">
                                        <label for="numero-supporti">Numero Supporti</label>
                                        <input type="number" class="form-control" name="numeroSupporti"
                                               id="numero-supporti" value="${a.numeroSupporti}" max="127" required>
                                        <div class="invalid-feedback">Inserisci il numero di dischi all'interno della
                                            confezione
                                        </div>
                                    </div>
                                </div>
                                <h5 class="mb-4">Aggiungi le canzoni all'album</h5>
                                <div id="canzoni">
                                    <c:forEach var="c" items="${a.canzoni}">
                                        <div class="form-row">
                                            <div class="form-group col-8">
                                                <label for="canzone-${c.posizione}">Canzone ${c.posizione}</label>
                                                <input type="text" class="form-control"
                                                       name="canzone-${c.posizione}"
                                                       id="canzone-${c.posizione}"
                                                       maxlength="45" value="${c.titolo}" required>
                                                <div class="invalid-feedback">Inserisci il nome della canzone (max 45
                                                    caratteri)
                                                </div>
                                            </div>
                                            <div class="form-group col-4">
                                                <label for="durata-${c.posizione}">Durata</label>
                                                <input type="time" step="1" class="form-control"
                                                       name="durata-${c.posizione}"
                                                       id="durata-${c.posizione}" value="${c.durata}"
                                                       required>
                                                <div class="invalid-feedback">Inserisci la durata della canzone</div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="d-flex justify-content-center">
                                    <button type="button" class="btn btn-danger rounded-circle mb-3 mr-3"
                                            id="rimuovi-canzone" onclick="rimuoviCanzone()">&ndash;
                                    </button>
                                    <button type="button" class="btn btn-success rounded-circle mb-3"
                                            id="aggiungi-canzone" onclick="aggiungiCanzone()">+
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${p.categoria.name() == 'FILM'}">
                        <%
                            Film f = (Film) dp;
                            pageContext.setAttribute("f", f);
                        %>
                        <div class="row justify-content-center">
                            <div class="col-md-5">
                                <h3>Dettagli Film</h3>
                                <div class="form-group">
                                    <label for="regia">Regia</label>
                                    <input type="text" class="form-control" name="regia" id="regia" value="${f.regia}"
                                           maxlength="45"
                                           required>
                                    <div class="invalid-feedback">Inserisci il nome del regista (max 45 caratteri)</div>
                                </div>
                                <div class="form-group">
                                    <label for="attori">Attori</label>
                                    <input type="text" class="form-control" name="attori" id="attori"
                                           value="${f.attori}" maxlength="255"
                                           required>
                                    <div class="invalid-feedback">Inserisci da 3 a 6 attori separati da virola (max 255
                                        caratteri)
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-6">
                                        <div class="form-check-label mb-2">Tipo di supporto</div>
                                        <label class="mr-5">
                                            <input type="radio" class="form-check form-check-inline" name="supporto"
                                                   value="DVD" ${f.supporto.name() == 'DVD' ? 'checked' : ''}>DVD
                                        </label>
                                        <label>
                                            <input type="radio" class="form-check form-check-inline" name="supporto"
                                                   value="BLURAY" ${f.supporto.name() == 'BLURAY' ? 'checked' : ''}>Blu-ray
                                        </label>
                                    </div>
                                    <div class="form-group col-6">
                                        <label for="paese">Paese</label>
                                        <input type="text" class="form-control" name="paese" id="paese"
                                               value="${f.paese}" maxlength="45"
                                               required>
                                        <div class="invalid-feedback">Inserisci il paese di produzione (max 45
                                            caratteri)
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%
                            Videogioco v = (Videogioco) dp;
                            pageContext.setAttribute("v", v);
                        %>
                        <div class="row justify-content-center">
                            <div class="col-md-5">
                                <h3>Dettagli Videogioco</h3>
                                <div class="form-group">
                                    <label for="produttore">Produttore</label>
                                    <input type="text" class="form-control" name="produttore" id="produttore"
                                           value="${v.produttore}" maxlength="45" required>
                                    <div class="invalid-feedback">Inserisci l'azienda sviluppatrice del gioco</div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-6">
                                        <label for="piattaforma">Piattaforma</label>
                                        <input type="text" class="form-control" name="piattaforma" id="piattaforma"
                                               value="${v.piattaforma}" list="piattaforme" maxlength="45" required>
                                        <datalist id="piattaforme">
                                            <option value="Xbox One"></option>
                                            <option value="PS4"></option>
                                            <option value="Nintendo Switch"></option>
                                            <option value="Nintendo 3DS"></option>
                                            <option value="PC Windows"></option>
                                            <option value="MacOS"></option>
                                        </datalist>
                                        <div class="invalid-feedback">Inserisci la piattaforma su cui il gioco può
                                            essere riprodotto
                                        </div>
                                    </div>
                                    <div class="form-group col-6">
                                        <label for="eta-consigliata">Età Consigliata</label>
                                        <input type="number" class="form-control" name="etaConsigliata"
                                               id="eta-consigliata" value="${v.etaConsigliata}" max="255" required>
                                        <div class="invalid-feedback">Inserisci l'età minima consigliata</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <input type="submit" class="btn btn-success btn-block col-md-5 mt-3 mx-auto" value="Modifica Prodotto">
        </form>
        <a href="${pageContext.request.contextPath}/admin/GestioneController?command=CANCELLA_PRODOTTO&id=${p.id}&categoria=${p.categoria.name()}"
           class="btn btn-danger btn-block col-md-5 mt-3 mx-auto">Cancella Prodotto</a>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
<script src="${pageContext.request.contextPath}/js/prodotto.js"></script>
</body>
</html>