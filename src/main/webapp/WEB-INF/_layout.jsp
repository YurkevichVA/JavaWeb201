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
    <link rel="stylesheet" href="styles.css">
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <title>Java web</title>
</head>
<body>

<nav>
    <div class="nav-wrapper green darken-4">
        <a href="<%= context %>/" class="brand-logo right">Java</a>
        <ul id="nav-mobile">
            <li><a href="<%= context %>/jsp">About</a></li>
            <li <%= "filters.jsp".equals(pageBody) ? "class='active'" : "" %> ><a href="<%= context %>/filters">Filters</a></li>
            <li <%= "ioc.jsp".equals(pageBody) ? "class='active'" : "" %> ><a href="<%= context %>/ioc">IoC</a></li>
        </ul>
    </div>
</nav>

<div class="container">
<jsp:include page="<%= pageBody%>"/>
</div>

<footer class="page-footer green darken-4">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Links</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2014 Copyright Text
            <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>




