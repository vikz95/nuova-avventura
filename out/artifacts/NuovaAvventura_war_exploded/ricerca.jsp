<%@ page import="business.Prodotto" %>
<%@ page import="util.Base64ImageEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" buffer="32kb" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Risultati Ricerca"/>
</jsp:include>

<div class="container py-5">
    <div id="griglia-prodotti">
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