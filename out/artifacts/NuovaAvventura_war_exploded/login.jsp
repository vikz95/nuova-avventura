<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Accedi"/>
</jsp:include>

<section id="login" class="my-5">
    <div class="container">
        <div class="row">
            <div class="col-md-6 mx-auto">
                <c:if test="${LOGIN_ERROR != null}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${LOGIN_ERROR}"/>
                    </div>
                </c:if>
                <div class="card">
                    <div class="card-header">
                        <h4>Accesso Utente</h4>
                    </div>
                    <div class="card-body">
                        <form action="AutenticazioneController" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="command" value="LOGIN">
                            <div class="input-group input-group-lg mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">
                                        <i class="far fa-envelope"></i>
                                    </span>
                                </div>
                                <input type="email" class="form-control" name="email" maxlength="45" required
                                       placeholder="Inserire Email">
                                <div class="invalid-feedback">Inserisci l'email (max 45 caratteri)</div>
                            </div>
                            <div class="input-group input-group-lg mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">
                                        <i class="fas fa-key"></i>
                                    </span>
                                </div>
                                <input type="password" class="form-control" name="password" placeholder="Inerire Password" minlength="6" required>
                                <div class="invalid-feedback">Inserisci la password (min 6 caratteri)</div>
                            </div>
                            <button type="submit" class="btn-info btn-block btn-lg">Accedi</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>