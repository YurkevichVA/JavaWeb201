<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!--Site CSS-->
    <link rel="stylesheet" href="<%=context%>/css/site.css?time=<%= new Date().getTime()%>">
    <title>Java web</title>
</head>
<body>

<header>
<nav>
    <div class="nav-wrapper brown darken-4">
        <a class="site-logo right" href="<%= context %>/">DnD Персонажи</a>
        <ul id="nav-mobile">
            <li <%= "login.jsp".equals(pageBody) ? "class='active'" : "" %> ><a href="<%= context %>/profile">Профіль</a></li>
            <li <%= "shop.jsp".equals(pageBody) ? "class='active'" : "" %> ><a href="<%= context %>/shop">Магазин</a></li>
        </ul>
    </div>
</nav>
</header>

<main>
<div class="container">
<jsp:include page="<%= pageBody%>"/>
</div>
</main>

<footer class="page-footer brown darken-4">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">DnD Персонажи</h5>
                <p class="grey-text text-lighten-4">Всі зображення згенеровані за допомогою штучного інтелекту.</p>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2023 It Step Uni
        </div>
    </div>
</footer>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<!-- Site JS -->
<script src="<%=context%>/js/site.js"></script>
</body>
</html>