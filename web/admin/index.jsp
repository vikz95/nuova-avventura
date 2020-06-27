<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Admin Area"/>
</jsp:include>

<section id="operazioni" class="my-5">
    <div class="container">
        <div class="row justify-content-around">
            <div class="col-md-3">
                <a href="aggiungi-prodotto.jsp">
                    <div class="card text-center py-3 bg-info text-white">
                        <h2>Aggiungi Prodotto</h2>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_ORDINI">
                    <div class="card text-center py-3 bg-success text-white">
                        <h2>Gestisci Ordini</h2>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PRENOTAZIONI">
                    <div class="card text-center py-3 bg-warning text-white">
                        <h2>Visualizza Prenotazioni</h2>
                    </div>
                </a>
            </div>
        </div>

        <div class="row justify-content-around mt-5">
            <div class="col-md-1"></div>
            <div class="col-md-3">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=VISUALIZZA_SPEDIZIONI">
                    <div class="card text-center py-3 bg-danger text-white">
                        <h2>Aggiorna Spedizioni</h2>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PAGAMENTI">
                    <div class="card text-center py-3 bg-info text-white">
                        <h2>Elenco Pagamenti</h2>
                    </div>
                </a>
            </div>
            <div class="col-md-1"></div>
        </div>
    </div>
</section>

<jsp:include page="../bootstrap-scripts.html"/>
</body>
</html>