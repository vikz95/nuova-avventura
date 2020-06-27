<%@ page import="business.Prodotto" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Nuova Avventura"/>
</jsp:include>

<section id="libri" class="py-5">
    <div class="container">
        <h3>Libri in evidenza</h3>
        <hr>
        <div class="row">
            <c:forEach var="p" items="${requestScope.libri}">
                <%
                    Prodotto p = (Prodotto) pageContext.getAttribute("p");
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                %>
                <div class="col-md-3">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${p.id}" class="card">
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
</section>

<section id="album" class="py-5">
    <div class="container">
        <h3>Album in evidenza</h3>
        <hr>
        <div class="row">
            <c:forEach var="p" items="${requestScope.album}">
                <%
                    Prodotto p = (Prodotto) pageContext.getAttribute("p");
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                %>
                <div class="col-md-3">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${p.id}" class="card">
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
</section>

<section id="film" class="py-5">
    <div class="container">
        <h3>Film in evidenza</h3>
        <hr>
        <div class="row">
            <c:forEach var="p" items="${requestScope.film}">
                <%
                    Prodotto p = (Prodotto) pageContext.getAttribute("p");
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                %>
                <div class="col-md-3">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${p.id}" class="card">
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
</section>

<section id="videogiochi" class="py-5">
    <div class="container">
        <h3>Videogiochi in evidenza</h3>
        <hr>
        <div class="row">
            <c:forEach var="p" items="${requestScope.videogiochi}">
                <%
                    Prodotto p = (Prodotto) pageContext.getAttribute("p");
                    pageContext.setAttribute("foto", Base64ImageEncoder.encode(p.getFoto()));
                %>
                <div class="col-md-3">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTO&id=${p.id}" class="card">
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
</section>

<jsp:include page="footer.jsp"/>