<%@ page import="business.Prodotto" %>
<%@ page import="business.ProdottoCarrello" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ page import="business.DettagliProdotto" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Carrello"/>
</jsp:include>

<section id="carrello" class="py-5">
    <div class="container">
        <div class="row justify-content-between">
            <div class="col-md-9 bg-white">
                <div class="cart">
                    <div class="cart-wrapper">
                        <div class="cart-header py-3 text-dark">
                            <div class="row">
                                <div class="col-5 text-left">Prodotti</div>
                                <div class="col-2 text-right">Prezzo</div>
                                <div class="col-2 text-center">Quantità</div>
                                <div class="col-2 text-right">Totale</div>
                                <div class="col-1"></div>
                            </div>
                        </div>
                        <c:set var="totale" value="0"/>
                        <div class="cart-body">
                            <%
                                ArrayList<ProdottoCarrello> prodottiCarrello = (ArrayList<ProdottoCarrello>) request.getAttribute("prodottiCarrello");
                                boolean procedi = prodottiCarrello.size() != 0;
                                pageContext.setAttribute("procedi", procedi);
                            %>
                            <c:forEach var="pc" items="${requestScope.prodottiCarrello}">
                                <%
                                    ProdottoCarrello pc = (ProdottoCarrello) pageContext.getAttribute("pc");
                                    Prodotto p = pc.getProdotto();
                                    DettagliProdotto dp = pc.getProdotto().getDettagliProdotto();
                                    pageContext.setAttribute("p", p);
                                    pageContext.setAttribute("dp", dp);
                                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));

                                    if (pc.getQuantita() > p.getQuantita()) {
                                        pageContext.setAttribute("procedi", false);
                                    }
                                %>
                                <c:set var="totale" value="${totale + p.costo * pc.quantita}"/>
                                <div class="cart-item border-bottom py-3">
                                    <div class="row">
                                        <div class="col-5">
                                            <div class="d-flex">
                                                <img src="data:image/jpg;base64,${foto}" class="cart-img mr-3">
                                                <div>
                                                    <div class="text-danger text-uppercase">
                                                        <strong>${p.categoria.name()}</strong></div>
                                                    <h5 class="crop-text-2">
                                                        <strong>${p.titolo}</strong>
                                                    </h5>
                                                    <c:choose>
                                                        <c:when test="${p.categoria.name() == 'FILM'}">
                                                            <div class="text-muted"><c:out value="${dp.regia}"/></div>
                                                        </c:when>
                                                        <c:when test="${p.categoria.name() == 'VIDEOGIOCHI'}">
                                                            <div class="text-muted"><c:out
                                                                    value="${dp.piattaforma}"/></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-muted"><c:out value="${dp.autori}"/></div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${p.quantita >= 10}">
                                                            <div class="text-success">Disponibilità immediata</div>
                                                        </c:when>
                                                        <c:when test="${p.quantita == 0}">
                                                            <div class="text-danger">Non disponibile</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-warning">Meno di 10 disponibili</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-2 text-primary text-right"><strong>${p.costo}€</strong></div>
                                        <div class="col-2 text-center">
                                            <form action="AcquistoController" method="post">
                                                <input type="hidden" name="command" value="AGGIORNA_CARRELLO">
                                                <input type="hidden" name="idProdotto" value="${p.id}">
                                                <div class="form-group">
                                                    <input type="number" name="quantita" min="1" max="${p.quantita}"
                                                           class="mx-auto form-control w-75 ${pc.quantita > p.quantita ? 'is-invalid' : ''}"
                                                           value="${pc.quantita}">
                                                    <div class="invalid-feedback">Solo più ${p.quantita} disponibili
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-outline-success mt-2">Aggiorna
                                                </button>
                                            </form>
                                        </div>
                                        <div class="col-2 text-primary text-right">
                                            <strong>${p.costo * pc.quantita}€</strong></div>
                                        <div class="col-1">
                                            <a href="AcquistoController?command=CANCELLA_CARRELLO&idProdotto=${p.id}">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <h3>Riepilogo ordine</h3>
                <hr>
                <div class="d-flex flex-column mb-3 justify-content-center align-items-end">
                    <span>Totale senza spese di spedizione:</span>
                    <span><h3>${totale}€</h3></span>
                </div>
                <c:if test="${}"></c:if>
                <a href="AcquistoController?command=GOTO_CHECKOUT&totaleParziale=${totale}"
                   class="btn btn-info btn-block btn-lg text-white ${procedi == false ? 'disabled' : ''}">
                    Procedi all'acquisto
                </a>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>