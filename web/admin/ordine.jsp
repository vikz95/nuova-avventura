<%@ page import="business.*" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Dettagli ordine"/>
</jsp:include>

<%
    Ordine o = (Ordine) request.getAttribute("ordine");
    pageContext.setAttribute("o", o);
    Indirizzo i = (Indirizzo) request.getAttribute("indirizzo");
    pageContext.setAttribute("i", i);
    TipologiaSpedizione ts = (TipologiaSpedizione) request.getAttribute("spedizione");
    pageContext.setAttribute("ts", ts);
    Pagamento p = (Pagamento) request.getAttribute("pagamento");
    pageContext.setAttribute("p", p);
    CartaCredito c = (CartaCredito) request.getAttribute("carta");
    pageContext.setAttribute("c", c);
%>

<section id="dettagli-ordine" class="py-5">
    <div class="container">
        <c:if test="${ORDINE_ERROR != null}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${ORDINE_ERROR}"/>
            </div>
        </c:if>
        <div class="d-flex justify-content-between">
            <h3 class="text-info mb-5">Ordine N°${o.id} del ${o.dataCreazione} (${o.stato.name()})</h3>
            <c:if test="${o.stato.name() == 'IN_CORSO'}">
                <div>
                    <button class="btn btn-danger h-50" data-toggle="modal" data-target="#annullaOrdine">
                        Annulla ordine
                    </button>
                    <button class="btn btn-success h-50" data-toggle="modal" data-target="#riceviPagamentoModal">
                        Ricevi Pagamento
                    </button>
                </div>
            </c:if>
            <c:if test="${o.stato.name() == 'PRONTO'}">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=SPEDISCI_ORDINE&id=${o.id}"
                   class="btn btn-primary h-50">Segna come spedito</a>
            </c:if>
        </div>
        <div class="row justify-content-between">
            <div class="col-md-3 card py-3">
                <h5>Indirizzo</h5>
                <div>${i.nome} ${i.cognome}</div>
                <div>${i.indirizzo}</div>
                <div>${i.CAP} - ${i.citta} (${i.provincia})</div>
            </div>
            <div class="col-md-3 card py-3">
                <h5>Spedizione</h5>
                <div class="text-capitalize">${ts.tipo}</div>
                <div>${o.dataSpedizione}</div>
            </div>
            <div class="col-md-3 card py-3">
                <h5>Pagamento</h5>
                <div>${c.intestatario}</div>
                <div>${c.numero}</div>
                <div>${p.stato.name()}</div>
                <div>${p.data}</div>
            </div>
        </div>

        <table class="table bg-white my-5">
            <thead>
            <tr>
                <th scope="col"></th>
                <th scope="col">Prodotto</th>
                <th scope="col">Prezzo</th>
                <th scope="col">Quantità</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="prodottoOrdine" items="${requestScope.prodottiOrdine}">
                <%
                    ProdottoOrdine po = (ProdottoOrdine) pageContext.getAttribute("prodottoOrdine");
                    pageContext.setAttribute("po", po);
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(po.getProdotto().getFoto()));
                %>
                <tr>
                    <td><img src="data:image/jpg;base64,${foto}" class="cart-img"></td>
                    <td>
                        <h6 class="text-danger">${po.prodotto.categoria.name()}</h6>
                        <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${po.prodotto.id}">
                            <p class="lead">${po.prodotto.titolo}</p>
                        </a>
                    </td>
                    <td>${po.prodotto.costo}€</td>
                    <td>${po.quantita}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<!--MODALS-->
<div class="modal fade" id="annullaOrdine">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title">Annulla Ordine</h5>
                <button class="close" data-dismiss="modal">
                    <span>&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p class="lead">L'ordine verrà annullato se non è ancora stato pagato.</p>
            </div>
            <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ANNULLA_ORDINE&id=${o.id}"
                   class="btn btn-danger">Conferma
                    Annullamento</a>
            </div>
        </div>
    </div>
</div>

<div class="modal fade show" id="riceviPagamentoModal" aria-modal="true">
    <div class="modal-dialog modal-lg">
        <form action="<c:url value="/admin/GestioneController" />" method="post">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title">Ricevi Pagamento</h5>
                    <button class="close" data-dismiss="modal">
                        <span class="text-white">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="command" value="RICEVI_PAGAMENTO">
                    <input type="hidden" name="idPagamento" value="${p.id}">
                    <input type="hidden" name="idOrdine" value="${o.id}">
                    <div class="form-group">
                        <div class="carta-credito mt-3">
                            <h4>Carta di credito</h4>
                            <hr>
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="intestatario">Intestatario</label>
                                    <input type="text" name="intestatario" id="intestatario" class="form-control"
                                           value="${c.intestatario}" readonly>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="numero-carta">Numero</label>
                                    <input type="number" name="numeroCarta" id="numero-carta" class="form-control"
                                           value="${c.numero}" readonly>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group col-md-4">
                                    <label for="scadenza">Data scadenza</label>
                                    <input type="month" name="scadenza" id="scadenza" class="form-control"
                                           value="${c.dataScadenza}" readonly>
                                </div>
                                <div class="form-group col-md-4">
                                    <label for="codice-sicurezza">Codice di sicurezza</label>
                                    <input type="number" name="codiceSicurezza" id="codice-sicurezza"
                                           class="form-control"
                                           value="${c.codiceSicurezza}" readonly>
                                </div>
                                <div class="form-group col-md-4">
                                    <label for="importo">Importo</label>
                                    <input type="number" name="importo" id="importo" class="form-control"
                                           value="${p.importo}" readonly>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Invia</button>
                </div>
            </div>
        </form>
    </div>
</div>


<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>