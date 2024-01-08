package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CharacterDao;
import step.learning.dto.models.CharacterFormModel;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;

@Singleton
public class AdminServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final CharacterDao characterDao;

    @Inject
    public AdminServlet(FormParseService formParseService, CharacterDao characterDao) {
        this.formParseService = formParseService;
        this.characterDao = characterDao;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // перевіряємо чи є повідомлення у сесії
        HttpSession session = req.getSession();
        Integer characterAddStatus = (Integer) session.getAttribute("character-add-status");
        if(characterAddStatus != null) { // є повідомлення
            // видаляємо його з сесії
            session.removeAttribute("character-add-status");
            // та передаємо дані у атрибути запиту
            String message ;
            if(characterAddStatus == 0) {
                message = "Помилка оброблення даних форми";
            }
            else if (characterAddStatus == 1) {
                message = "Помилка валідації даних форми";
                req.setAttribute("character-model", session.getAttribute("character-model"));
                session.removeAttribute("character-model");
            }
            else {
                message = "Додавання успішне";
            }
            req.setAttribute("character-add-message", message);
        }
        req.setAttribute("page-body", "admin.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        characterDao.install();

        FormParseResult formParseResult = formParseService.parse(req);
        CharacterFormModel model;

        try {
            model = new CharacterFormModel(formParseResult);
        } catch (ParseException e) {
            e.printStackTrace();
            model = null;
        }

        HttpSession session = req.getSession();

        // Зберігаємо необхідні дані у сесії та повертаємо на ГЕТ шляхом відповіді-редиректу
        if(model == null) {
            // стан помилки розбору форм
            session.setAttribute("character-add-status", 0);
        }
        else if(!model.getErrorMessages().isEmpty()) {
            // стан помилки валідації - зберігаємо саму модель
            // для відновлення даних на формі введення
            session.setAttribute("character-model", model);
            session.setAttribute("character-add-status", 1);
        }
        else {
            // стан успішної обробки моделі - передаємо лише повідомлення
            if(characterDao.addFromForm(model)) {
                session.setAttribute("character-add-status", 2);
            }
        }
        resp.sendRedirect(req.getRequestURI());
    }
}