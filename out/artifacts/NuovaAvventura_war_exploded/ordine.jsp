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
                <button class="btn btn-danger h-50" data-toggle="modal" data-target="#annullaOrdine">Annulla ordine</button>
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
                <a href="AcquistoController?command=ANNULLA_ORDINE&id=${o.id}" class="btn btn-danger">Conferma
                    Annullamento</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>