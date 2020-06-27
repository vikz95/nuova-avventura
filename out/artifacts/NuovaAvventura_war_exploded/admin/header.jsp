<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title><c:out value="${param.title}"/></title>
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark sticky-top bg-info">
    <div class="container">
        <a href="${pageContext.request.contextPath}/admin/index.jsp" class="navbar-brand">Admin Area</a>
        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/NavigazioneController?command=MOSTRA_HOME" class="nav-link">Home Sito</a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PRODOTTI&categoria=LIBRI"
                       class="nav-link">Libri</a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PRODOTTI&categoria=ALBUM"
                       class="nav-link">Album Musicali</a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PRODOTTI&categoria=FILM"
                       class="nav-link">Film</a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/GestioneController?command=ELENCA_PRODOTTI&categoria=VIDEOGIOCHI"
                       class="nav-link">Videogiochi</a>
                </li>
            </ul>
        </div>
    </div>
</nav>