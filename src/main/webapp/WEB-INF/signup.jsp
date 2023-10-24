<%@ page import="step.learning.dto.models.RegFormModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Перевіряємо чи є повідомлення попередньої форми, формуємо значення полів
    RegFormModel model = ( RegFormModel ) request.getAttribute("model");
    String loginValue = model == null ? "" : model.getLogin();
    String nameValue = model == null ? "" : model.getName();
    String emailValue = model == null ? "" : model.getEmail();
    //String birthdateValue = model == null ? "" : model.getBirthdateAsString();
    Map<String, String> errors = model == null ? new HashMap<String, String>() : (HashMap<String, String>) model.getErrorMessages();
%>

<h2>Реєстрація користувача</h2>
<p><%=request.getAttribute("reg-message")%></p>
<div class="row">
    <form class="col s12" method="post">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">person</i>
                <input value="<%=loginValue%>" name="reg-login" id="reg-login" type="text" class=<%=errors.containsKey("login") ? "invalid" : ""%>>
                <label for="reg-login">Логін</label>
                <% if(errors.containsKey("login")) { %>
                <span class="helper-text" data-error="<%=errors.get("login")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">badge</i>
                <input value="<%=nameValue%>" name="reg-name" id="reg-name" type="tel" class=<%=errors.containsKey("name") ? "invalid" : ""%>>
                <label for="reg-name">Ім'я</label>
                <% if(errors.containsKey("name")) { %>
                <span class="helper-text" data-error="<%=errors.get("name")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input value="<%=emailValue%>" name="reg-email" id="reg-email" type="email" class=<%=errors.containsKey("email") ? "invalid" : ""%>>
                <label for="reg-email">E-mail</label>
                <% if(errors.containsKey("email")) { %>
                <span class="helper-text" data-error="<%=errors.get("email")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">cake</i>
                <input name="reg-birthdate" id="reg-birthdate" type="date" class=<%=errors.containsKey("birthdate") ? "invalid" : ""%>>
                <label for="reg-birthdate">Дата народження</label>
                <% if(errors.containsKey("birthdate")) { %>
                <span class="helper-text" data-error="<%=errors.get("birthdate")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input name="reg-password" id="reg-password" type="password" class=<%=errors.containsKey("password") ? "invalid" : ""%>>
                <label for="reg-password">Пароль</label>
                <% if(errors.containsKey("password")) { %>
                <span class="helper-text" data-error="<%=errors.get("password")%>" ></span>
                <% } %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">cake</i>
                <input name="reg-repeat" id="reg-repeat" type="password" class=<%=errors.containsKey("repeat") ? "invalid" : ""%>>
                <label for="reg-repeat">Повторіть пароль</label>
                <% if(errors.containsKey("repeat")) { %>
                <span class="helper-text" data-error="<%=errors.get("repeat")%>" ></span>
                <% } %>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <label for="reg-rules" class=<%=errors.containsKey("rules") ? "invalid" : ""%>>
                    <input id="reg-rules" name="reg-rules" type="checkbox" class="filled-in" />
                    <span>Не буду нічо порушувати</span>
                    <% if(errors.containsKey("rules")) { %>
                    <span class="helper-text" data-error="<%=errors.get("rules")%>" ></span>
                    <% } %>
                </label>
            </div>
            <div class="input-field col s6 right-align">
            <button class="waves-effect waves-light btn green accent-4">Реєстрація</button>
            </div>
        </div>
    </form>
</div>
