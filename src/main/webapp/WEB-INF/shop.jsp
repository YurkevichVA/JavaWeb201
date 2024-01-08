<%@ page import="java.util.List" %>
<%@ page import="step.learning.dto.entities.Character" %>
<%@ page import="org.checkerframework.checker.units.qual.C" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String context = request.getContextPath(); %>
<h1>Магазин</h1>

<div class="row">
    <%
        List<Character> characters = (List<Character>) request.getAttribute("characters");
        for (Character character : characters) {
    %>
    <div class="col s12 m6">
        <div class="card brown">
            <div class="card-image">
                <img src="<%= context %>/upload/characters/<%= character.getImage() %>" alt="<%= character.getName() %>">
                <span class="card-title"><%= character.getName() %></span>
                <a class="btn-floating halfway-fab waves-effect waves-light brown lighten-1 buy-character-btn" role="button" data-character-id="<%= character.getId() %>"
                   data-character-cost="<%= character.getCost() %>">
                    <i class="material-icons">add</i>
                </a>
            </div>
            <div class="card-content">
                <p>Вартість: <b><%= character.getCost() %></b> $</p>
            </div>
        </div>
    </div>
    <% } %>
</div>