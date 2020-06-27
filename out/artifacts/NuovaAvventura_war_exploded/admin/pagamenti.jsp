<%@ page import="business.Pagamento" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Pagamenti"/>
</jsp:include>

<section id="elenco-pagamenti" class="py-5">
    <div class="container">
        <table class="table table-hover">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Data</th>
                <th scope="col">Stato</th>
                <th scope="col">Importo</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="pagamento" items="${requestScope.pagamenti}">
                <%
                    Pagamento p = (Pagamento) pageContext.getAttribute("pagamento");
                    pageContext.setAttribute("p", p);
                %>
                <tr>
                    <td>${p.id}</td>
                    <td>${p.data}</td>
                    <td>${p.stato}</td>
                    <td>${p.importo}€</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>
