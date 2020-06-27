<%@ page import="business.Prodotto" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="${param.categoria}"/>
</jsp:include>

<div class="container py-5">
    <div id="griglia-prodotti">
        <header class="header-griglia-prodotti d-flex justify-content-between align-items-baseline">
            <div>
                <c:choose>
                    <c:when test="${param.categoria == 'LIBRI' && param.tipo == null}">
                        <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&tipo=FUMETTO"
                           class="btn btn-outline-secondary">Solo Fumetti</a>
                    </c:when>
                    <c:when test="${param.categoria == 'ALBUM'}">
                        <div class="btn-group" role="group">
                            <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&tipo=CD"
                               class="btn btn-outline-secondary">CD</a>
                            <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&tipo=VINILE"
                               class="btn btn-outline-secondary">VINILI</a>
                        </div>
                    </c:when>
                    <c:when test="${param.categoria == 'FILM'}">
                        <div class="btn-group" role="group">
                            <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&tipo=DVD"
                               class="btn btn-outline-secondary">DVD</a>
                            <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&tipo=BLURAY"
                               class="btn btn-outline-secondary">BLURAY</a>
                        </div>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
            </div>
            <div class="mb-3 d-flex align-items-center">
                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Ordina per
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=titolo">A-Z</a>
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=titolo DESC">Z-A</a>
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=costo">Prezzo
                            Crescente</a>
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=costo DESC">Prezzo
                            Decrescente</a>
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=media_recensioni DESC">Media
                            Recensioni</a>
                        <a class="dropdown-item"
                           href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=${param.categoria}&ordinamento=data_caricamento DESC">Ultimi
                            Arrivi</a>
                    </div>
                </div>
            </div>
        </header>
        <div class="row">
            <c:forEach var="prodotto" items="${requestScope.prodotti}">
                <%
                    Prodotto p = (Prodotto) pageContext.getAttribute("prodotto");
                    pageContext.setAttribute("p", p);
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                %>
                <div class="col-md-3 pb-5">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${p.id}" class="card rounded">
                        <img src="data:image/jpg;base64,${foto}" class="img-fluid">
                        <div class="card-body">
                            <div class="stelle text-center mb-3">
                                <c:forEach begin="1" end="${p.stelle}">
                                    <i class="fas fa-star text-warning"></i>
                                </c:forEach>
                                <c:forEach begin="${p.stelle + 1}" end="4">
                                    <i class="far fa-star text-warning"></i>
                                </c:forEach>
                            </div>
                            <h5 class="card-title crop-text-2 text-center">${p.titolo}</h5>
                            <div class="prezzo text-center">
                                <h4><span class="badge badge-pill badge-info badge">${p.costo}€</span></h4>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>