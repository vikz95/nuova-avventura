<%@ page import="business.Ordine" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Ordini"/>
</jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<section id="elenco-ordini" class="py-5">
    <div class="container">
        <table class="table table-hover">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Data</th>
                <th scope="col">ID Utente</th>
                <th scope="col">Stato</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ordine" items="${ordini}">
                <%
                    Ordine o = (Ordine) pageContext.getAttribute("ordine");
                    pageContext.setAttribute("o", o);
                %>
                <tr>
                    <th scope="row">${o.id}</th>
                    <td>${o.dataCreazione}</td>
                    <td>${o.idUtente}</td>
                    <c:choose>
                        <c:when test="${o.stato.name() == 'IN_CORSO'}">
                            <td class="text-warning">In corso</td>
                        </c:when>
                        <c:when test="${o.stato.name() == 'PRONTO'}">
                            <td class="text-success">Pronto</td>
                        </c:when>
                        <c:when test="${o.stato.name() == 'SPEDITO'}">
                            <td class="text-primary">Spedito</td>
                        </c:when>
                        <c:otherwise>
                            <td class="text-danger">Annullato</td>
                        </c:otherwise>
                    </c:choose>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/GestioneController?command=GET_ORDINE&id=${o.id}"
                           class="btn btn-info">Dettagli</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>