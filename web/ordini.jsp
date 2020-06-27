<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="I miei ordini"/>
</jsp:include>

<section id="elenco-ordini" class="py-5">
    <div class="container">
        <table class="table table-hover">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Data</th>
                <th scope="col">Stato</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${ordini}">
                <tr>
                    <th scope="row">${o.id}</th>
                    <td>${o.dataCreazione}</td>
                    <td>${o.stato.name()}</td>
                    <td>
                        <a href="AcquistoController?command=VISUALIZZA_ORDINE&idOrdine=${o.id}" class="btn btn-info">Dettagli</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="footer.jsp"/>