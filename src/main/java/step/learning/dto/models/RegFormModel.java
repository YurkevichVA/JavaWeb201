package step.learning.dto.models;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegFormModel {
    // region fields
    private String name;
    private String login;
    private String password;
    private String repeat;
    private String email;
    private Date birthdate;
    private boolean isAgree;
    // endregion


    public RegFormModel( HttpServletRequest request ) throws ParseException {
        this.setName(request.getParameter("reg-name"));
        this.setLogin(request.getParameter("reg-login"));
        this.setPassword(request.getParameter("reg-password"));
        this.setRepeat(request.getParameter("reg-repeat"));
        this.setEmail(request.getParameter("reg-email"));
        this.setIsAgree(request.getParameter("reg-rules"));
        this.setBirthdate(request.getParameter("reg-birthdate"));

    }

    public Map<String, String > getErrorMessages() {
        Map<String, String> result = new HashMap<>() ;

        if(login == null || "".equals(login)) {
            result.put("login", "Логін не може бути порожнім");
        }

        if(name == null || "".equals(name)) {
            result.put("name", "Ім'я не може бути порожнім");
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        if(!pattern.matcher(email).matches()) {
            result.put("email", "Некоректна адреса електронної пошти");
        }

        Date current = new Date();

        if(birthdate == null) {
            result.put("birthdate", "Треба ввести дату народження");
        }
        else if(birthdate.compareTo( current ) > 0 ) {
            result.put("birthdate", "Вітаємо, мандрівнику у часі! Як там третя світова?");
        }

        if(password == null || "".equals(password)) {
            result.put("password", "Пароль не може бути порожнім");
        }
        else if(password.length() < 3) {
            result.put("password", "Пароль повинен бути більше трьох символів");
        }

        if(repeat == null || "".equals(repeat)) {
            result.put("repeat", "Необхідно повторити пароль");
        }
        else if(!password.equals(repeat)) {
            result.put("repeat", "Паролі не співпадають");
        }

        if(!isAgree) {
            result.put("rules", "Треба погодитись шоб нічо не порушувати");
        }

        return result;
    }

    // region accessons
    public String getBirthdateAsString() {
        return formDate.format(getBirthdate());
    }

    public void setBirthdate(String birthdate) throws ParseException {
        if(birthdate == null || "".equals(birthdate)) {
            this.birthdate = null;
        }
        else {
            this.birthdate = formDate.parse(birthdate);
        }
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = "on".equalsIgnoreCase(isAgree) || "true".equalsIgnoreCase(isAgree) ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }
    // endregion

    private static final SimpleDateFormat formDate = new SimpleDateFormat("yyyy-MM-dd");
}
