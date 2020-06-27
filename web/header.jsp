<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <title><c:out value="${param.title}"/></title>
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a href="NavigazioneController?command=MOSTRA_HOME" class="navbar-brand">Nuova Avventura</a>
        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=LIBRI" class="nav-link">Libri</a>
                </li>
                <li class="nav-item">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=ALBUM" class="nav-link">Album Musicali</a>
                </li>
                <li class="nav-item">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=FILM" class="nav-link">Film</a>
                </li>
                <li class="nav-item">
                    <a href="NavigazioneController?command=MOSTRA_PRODOTTI&categoria=VIDEOGIOCHI" class="nav-link">Videogiochi</a>
                </li>
            </ul>
            <form action="NavigazioneController" class="form-inline">
                <input type="hidden" name="command" value="RISULTATI_RICERCA">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <select class="custom-select bg-light" name="categoria" id="select-categoria">
                            <option value="" selected>CERCA IN</option>
                            <option value="LIBRI">LIBRI</option>
                            <option value="ALBUM">ALBUM</option>
                            <option value="FILM">FILM</option>
                            <option value="VIDEOGIOCHI">VIDEOGIOCHI</option>
                        </select>
                    </div>
                    <input class="form-control" type="search" name="termini" placeholder="Cerca" minlength="4" required>
                    <div class="input-group-append">
                        <button class="btn btn-secondary" type="submit">Cerca</button>
                    </div>
                </div>
            </form>
            <div class="cart ml-4 text-white">
                <a href="AcquistoController?command=MOSTRA_CARRELLO">
                    <i class="fas fa-shopping-cart fa-2x"></i>
                </a>
            </div>
            <div class="dropdown ml-1">
                <c:choose>
                    <c:when test="${sessionScope.idUtente != null}">
                        <button class="btn text-success dropdown-toggle" type="button" id="dropdownMenuButton"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-user fa-2x"></i>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn text-white dropdown-toggle" type="button" id="dropdownMenuButton"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-user fa-2x"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <c:choose>
                        <c:when test="${sessionScope.idUtente != null}">
                            <a class="dropdown-item" href="AcquistoController?command=VISUALIZZA_ORDINI">I miei ordini</a>
                            <a class="dropdown-item" href="AutenticazioneController?command=LOGOUT">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <a class="dropdown-item" href="login.jsp">Accedi</a>
                            <a class="dropdown-item" href="signup.jsp">Registrati</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</nav>
