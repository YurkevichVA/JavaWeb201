<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="step.learning.dto.models.CharacterFormModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Перевіряємо чи є повідомлення попередньої форми, формуємо значення полів
    CharacterFormModel model = ( CharacterFormModel ) request.getAttribute("character-model");
    String nameValue = model == null ? "" : model.getName();
    Integer costValue = model == null ? 0 : model.getCost();
    Map<String, String> errors = model == null ? new HashMap<String, String>() : (HashMap<String, String>) model.getErrorMessages();

    String nameClass = model == null ? "validate" : ( errors.containsKey("name") ? "invalid" : "valid");
    String costClass = model == null ? "validate" : ( errors.containsKey("cost") ? "invalid" : "valid");
    String imageClass = model == null ? "validate" : ( errors.containsKey("image") ? "invalid" : "valid");

    String characterAddMessage = (String) request.getAttribute("character-add-message");
    if(characterAddMessage == null) {
        characterAddMessage = "";
    }
%>
<h1>Адмін панель</h1>
<p><%=characterAddMessage%></p>
<div class="row">
    <form class="col s12" method="post" enctype="multipart/form-data">

        <!-- Name & Cost Inputs -->
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">mode_edit</i>
                <input id="icon_prefix" name="character-name" type="text" class="validate" value="<%=nameValue%>" class=<%=nameClass%>>
                <label for="icon_prefix">Назва</label>
                <% if(errors.containsKey("name")) { %>
                <span class="helper-text" data-error="<%=errors.get("name")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">attach_money</i>
                <input id="icon_telephone" name="character-cost" type="number" class="validate" value="<%=costValue%>" class=<%=costClass%>>
                <label for="icon_telephone">Вартість</label>
                <% if(errors.containsKey("cost")) { %>
                <span class="helper-text" data-error="<%=errors.get("cost")%>" ></span>
                <% } %>
            </div>
        </div>
        <!---->

        <!-- File Input -->
        <div class="file-field input-field">
            <div class="btn brown lighten-1">
                <span>Обрати зображення</span>
                <input type="file" name="character-image" class=<%=imageClass%>>
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate" type="text" >
                <% if(errors.containsKey("image")) { %>
                <span class="helper-text" data-error="<%=errors.get("image")%>" ></span>
                <% } %>
            </div>
        </div>
        <!---->

        <button class="btn waves-effect waves-light brown lighten-1" type="submit" name="action">Додати</button>
    </form>
</div>