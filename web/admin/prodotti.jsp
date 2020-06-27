<%@ page import="business.Prodotto, util.Base64ImageEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="${requestScope.categoria}"/>
</jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section id="prodotti" class="my-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="elenco-prodotti">
                <div class="py-3 text-dark">
                    <div class="row">
                        <div class="col-5 text-left">Prodotto</div>
                        <div class="col-2 text-right">SKU</div>
                        <div class="col-2 text-right">Prezzo</div>
                        <div class="col-2 text-center">Quantità</div>
                        <div class="col-1"></div>
                    </div>
                </div>
                <c:forEach var="p" items="${requestScope.prodotti}">
                    <%
                        Prodotto p = (Prodotto) pageContext.getAttribute("p");
                        pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                        pageContext.setAttribute("dp", p.getDettagliProdotto());
                    %>
                    <div class="border-bottom py-3">
                        <div class="row">
                            <div class="col-5">
                                <div class="d-flex">
                                    <img src="data:image/jpg;base64,${foto}" class="cart-img mr-3">
                                    <div>
                                        <div class="text-danger small text-uppercase">
                                            <strong>${requestScope.categoria}</strong></div>
                                        <h5 class="crop-text-2">
                                            <strong>${p.titolo}</strong>
                                        </h5>
                                        <c:choose>
                                            <c:when test="${requestScope.categoria == 'FILM'}">
                                                <div class="text-muted"><c:out value="${dp.regia}"/></div>
                                            </c:when>
                                            <c:when test="${requestScope.categoria == 'VIDEOGIOCHI'}">
                                                <div class="text-muted"><c:out value="${dp.piattaforma}"/></div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="text-muted"><c:out value="${dp.autori}"/></div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <div class="col-2 text-primary text-right"><c:out value="${p.sku}"/></div>
                            <div class="col-2 text-primary text-right"><c:out value="${p.costo}"/>€</div>
                            <div class="col-2 text-primary text-center">
                                <strong><c:out value="${p.quantita}"/></strong>
                            </div>
                            <div class="col-1">
                                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=GET_PRODOTTO&id=${p.id}">
                                    <i class="fas fa-pencil-alt"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>
