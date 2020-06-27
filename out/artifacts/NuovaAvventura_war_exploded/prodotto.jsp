<%@ page import="business.Prodotto" %>
<%@ page import="business.DettagliProdotto" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="business.Recensione" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%
    Prodotto p = (Prodotto) request.getAttribute("prodotto");
    DettagliProdotto dp = p.getDettagliProdotto();
    ArrayList<Recensione> recensioni = (ArrayList<Recensione>) request.getAttribute("recensioni");
    pageContext.setAttribute("p", p);
    pageContext.setAttribute("dp", dp);
    pageContext.setAttribute("recensioni", recensioni);
    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
%>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="${p.titolo}"/>
</jsp:include>

<section id="prodotto">
    <div class="container">
        <c:if test="${PRODUCT_ERROR != null}">
            <div class="alert alert-danger mt-3 alert-dismissible fade show" role="alert">
                <c:out value="${PRODUCT_ERROR}"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <c:if test="${PRODUCT_SUCCESS != null}">
            <div class="alert alert-success mt-3 alert-dismissible fade show" role="alert">
                <c:out value="${PRODUCT_SUCCESS}"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <div class="row py-5">
            <div class="col-md-4 pr-5">
                <img src="data:image/jpg;base64,${foto}" class="img-fluid pl-5" style="max-height: 400px">
            </div>
            <div class="col-md-4">
                <h3>${p.titolo}</h3>
                <c:if test="${p.categoria.name() == 'LIBRI'}">
                    <p>di <strong class="text-secondary">${dp.autori}</strong></p>
                </c:if>
                <div class="stelle mb-3">
                    <c:forEach begin="1" end="${p.stelle}">
                        <i class="fas fa-star text-warning"></i>
                    </c:forEach>
                    <c:forEach begin="${p.stelle + 1}" end="4">
                        <i class="far fa-star text-warning"></i>
                    </c:forEach>
                    <span class="ml-3 text-muted"><%= recensioni.size()%> recensioni</span>
                    <span class="mx-2 text-primary">|</span>
                    <span><a href="#" data-toggle="modal" data-target="#scriviRecensioneModal">Scrivi una recensione</a></span>
                </div>
                <hr class="my-4">
                <c:choose>
                    <c:when test="${p.categoria.name() == 'LIBRI'}">
                        <p><strong>Editore: </strong>${dp.editore}</p>
                        <c:if test="${dp.fumetto}">
                            <p><strong>Tipo: </strong>Fumetto</p>
                        </c:if>
                        <p><strong>Anno edizione: </strong>${p.anno}</p>
                        <p><strong>Pagine: </strong>${dp.numeroPagine}</p>
                    </c:when>
                    <c:when test="${p.categoria.name() == 'ALBUM'}">
                        <p><strong>Artisti: </strong>${dp.autori}</p>
                        <p><strong>Supporto: </strong>${dp.supporto.name()}</p>
                        <p><strong>Numero supporti: </strong>${dp.numeroSupporti}</p>
                        <p><strong>Anno di pubblicazione: </strong>${p.anno}</p>
                    </c:when>
                    <c:when test="${p.categoria.name() == 'FILM'}">
                        <p><strong>Regia: </strong>${dp.regia}</p>
                        <p><strong>Interpreti: </strong>${dp.attori}</p>
                        <p><strong>Paese: </strong>${dp.paese}</p>
                        <p><strong>Anno: </strong>${p.anno}</p>
                        <p><strong>Supporto: </strong>${dp.supporto.name()}</p>
                    </c:when>
                    <c:otherwise>
                        <p><strong>Piattaforma: </strong>${dp.piattaforma}</p>
                        <p><strong>Produttore: </strong>${dp.produttore}</p>
                        <p><strong>Anno: </strong>${p.anno}</p>
                        <p><strong>Età consigliata: </strong>${dp.etaConsigliata}+</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-4 pl-5">
                <div class="card">
                    <div class="card-body d-flex flex-column align-items-center">
                        <div class="prezzo mb-3">
                            <h3>
                                <span class="badge badge-pill badge-primary text-white badge">${p.costo}€</span>
                            </h3>
                        </div>
                        <c:choose>
                            <c:when test="${p.quantita >= 10}">
                                <div class="mb-3 text-success">Disponibilità immediata</div>
                            </c:when>
                            <c:when test="${p.quantita == 0}">
                                <div class="mb-3 text-danger">Non disponibile</div>
                            </c:when>
                            <c:otherwise>
                                <div class="mb-3 text-warning">Meno di 10 disponibili</div>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${p.quantita == 0}">
                                <form action="NavigazioneController" method="post">
                                    <input type="hidden" name="command" value="PRENOTA">
                                    <input type="hidden" name="idProdotto" value="${p.id}">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">Quantità</span>
                                        </div>
                                        <input type="number" class="form-control" value="1" min="1" name="quantita"
                                               id="quantita1">
                                    </div>
                                    <button class="btn btn-info btn-block" type="submit">Prenota ora</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="AcquistoController" method="post">
                                    <input type="hidden" name="command" value="AGGIUNGI_CARRELLO">
                                    <input type="hidden" name="idProdotto" value="${p.id}">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">Quantità</span>
                                        </div>
                                        <input type="number" class="form-control" value="1" min="1" name="quantita"
                                               id="quantita2">
                                    </div>
                                    <button class="btn btn-info btn-block" type="submit">Aggiungi al carrello</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="descrizione-recensioni" class="mb-5">
    <div class="container">
        <ul role="tablist" class="nav nav-tabs">
            <li class="nav-item">
                <a href="#descrizione" data-toggle="tab" role="tab" class="nav-link active"
                   aria-selected="true">Descrizione</a>
            </li>
            <c:if test="${p.categoria.name() == 'ALBUM'}">
                <li class="nav-item">
                    <a href="#canzoni" data-toggle="tab" role="tab" class="nav-link"
                       aria-selected="false">Canzoni</a>
                </li>
            </c:if>
            <li class="nav-item" id="recensioniTab">
                <a href="#recensioni" data-toggle="tab" role="tab" class="nav-link"
                   aria-selected="false">Recensioni <i class="fas fa-sort"></i></a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="descrizione" role="tabpanel"
                 class="tab-pane show fade bg-white px-5 py-3 w-75 active">${p.descrizione}</div>
            <c:if test="${p.categoria.name() == 'ALBUM'}">
                <div id="canzoni" role="tabpanel" class="tab-pane bg-white px-5 py-3 w-75 fade">
                    <table class="table table-sm">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Titolo</th>
                            <th scope="col">Durata</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="c" items="${dp.canzoni}">
                            <tr>
                                <th scope="row">${c.posizione}</th>
                                <td>${c.titolo}</td>
                                <td>${c.durata}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div id="recensioni" role="tabpanel" class="tab-pane bg-white px-5 py-3 w-75 fade"></div>
        </div>
    </div>
</section>

<!--MODALS-->

<div class="modal fade" id="scriviRecensioneModal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title">Aggiungi la tua recensione</h5>
                <button class="close" data-dismiss="modal">
                    <span>&times;</span>
                </button>
            </div>
            <form action="NavigazioneController" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="command" value="SCRIVI_RECENSIONE">
                <input type="hidden" name="idProdotto" value="${p.id}">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="star-rating">Rating</label>
                        <div class="star-rating__stars" id="star-rating">
                            <input class="star-rating__input custom-control-input" type="radio" name="rating" value="1"
                                   id="rating-1" required/>
                            <label class="star-rating__label" for="rating-1"></label>
                            <input class="star-rating__input custom-control-input" type="radio" name="rating" value="2"
                                   id="rating-2"/>
                            <label class="star-rating__label" for="rating-2"></label>
                            <input class="star-rating__input custom-control-input" type="radio" name="rating" value="3"
                                   id="rating-3"/>
                            <label class="star-rating__label" for="rating-3"></label>
                            <input class="star-rating__input custom-control-input" type="radio" name="rating" value="4"
                                   id="rating-4"/>
                            <label class="star-rating__label" for="rating-4"></label>
                            <div class="invalid-feedback" style="margin-left: 130px">Dai una valutazione al prodotto da
                                1 a 4 stelle
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nickname">Il tuo nome</label>
                        <input type="text" class="form-control" name="nickname" id="nickname" maxlength="45" required>
                        <div class="invalid-feedback">Inserisci il tuo nome completo o un nickname se desideri privacy
                            (max 45 caratteri)
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="testo">Recensione</label>
                        <textarea class="form-control" rows="7" name="testo" id="testo" minlength="80"
                                  maxlength="512" required></textarea>
                        <div class="invalid-feedback">Inserisci il testo della recensione (da 80 a 512 caratteri)</div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-info ml-auto" value="Aggiungi">Aggiungi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const recensioni = document.getElementById('recensioni');
    const recensioniTab = document.getElementById('recensioniTab');

    const recensioniArray = [];
    <c:forEach var="r" items="${recensioni}">
    recensioniArray.push({
        nickname: `${r.nickname}`,
        stelle: ${r.stelle},
        data: new Date(`${r.data}`),
        testo: `${r.testo}`
    });
    </c:forEach>

    let clicks = 0;
    recensioniTab.addEventListener('click', function () {
        clicks++;
        if (clicks % 2 === 1) {
            // ordina recensioni per numero decrescente di stelle
            recensioniArray.sort(function (a, b) {
                return b.stelle - a.stelle;
            });
        } else {
            // ordina recensioni per numero decrescente di stelle
            recensioniArray.sort(function (a, b) {
                return a.stelle - b.stelle;
            });
        }
        let html = '';
        recensioniArray.forEach(function (r) {
            html += getRecensioneHtml(r);
        });
        recensioni.innerHTML = html;
    });

    function getRecensioneHtml(r) {
        const htmlStelle = getStelleHtml(r);
        return '<div class="recensione">' +
            '<div class="d-flex justify-content-between">' +
            '<h5>' + r.nickname + '</h5>' +
            '<span class="text-muted">' + r.data.getDate() + '/' + (r.data.getMonth() + 1) + '/' + r.data.getFullYear() + '</span>' +
            '</div>' +
            '<div class="stelle mb-3">' +
            htmlStelle +
            '</div>' +
            '<p class="text-muted">' + r.testo + '</p>' +
            '</div>' +
            '<hr>';
    }

    function getStelleHtml(r) {
        let htmlStelle = '';
        for (let i = 1; i <= r.stelle; i++) {
            htmlStelle += '<i class="fas fa-star text-warning"></i>';
            console.log(i);
        }
        for (let i = r.stelle + 1; i <= 4; i++) {
            htmlStelle += '<i class="far fa-star text-warning"></i>';
            console.log(i);
        }
        return htmlStelle;
    }
</script>

<jsp:include page="footer.jsp"/>
