<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Aggiorna Spedizioni"/>
</jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section id="prezzo-spedizioni" class="my-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <h2 class="mb-4">Costo Spedizioni</h2>
                <form action="<c:url value="/admin/GestioneController"/>" method="post" class="needs-validation"
                      novalidate>
                    <input type="hidden" name="command" value="AGGIORNA_SPEDIZIONI">
                    <div class="form-group">
                        <label for="posta">Spedizione per Posta</label>
                        <input type="number" name="posta" id="posta" value="${requestScope.posta.costo}"
                               class="form-control" min="0"
                               max="9999.99" step=".01" required>
                    </div>
                    <div class="form-group">
                        <label for="corriere">Spedizione con Corriere Espresso</label>
                        <input type="number" name="corriere" id="corriere" value="${requestScope.corriere.costo}"
                               class="form-control" min="0" max="9999.99" step=".01" required>
                    </div>
                    <input type="submit" class="btn btn-primary  btn-block" value="Aggiorna">
                </form>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>
