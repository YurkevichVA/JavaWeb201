package step.learning.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.CharacterDao;
import step.learning.dao.UserDao;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.Character;
import step.learning.dto.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ShopServlet extends HttpServlet {
    private CharacterDao characterDao;
    private AuthTokenDao authTokenDao;
    private UserDao userDao;

    @Inject
    public ShopServlet(CharacterDao characterDao, AuthTokenDao authTokenDao, UserDao userDao) {
        this.characterDao = characterDao;
        this.authTokenDao = authTokenDao;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Character> characters = characterDao.getAll();

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    AuthToken authToken = authTokenDao.getTokenByBearer(token);
                    if(authToken != null) {
                        User user = userDao.getUserById(Long.parseLong(authToken.getSub()));
                        if(user != null) {
                            characters = characterDao.getAllExceptOwned( Long.parseLong( user.getId() ) );
                        }
                    }
                    break;
                }
            }
        }

        req.setAttribute("characters", characters);
        req.setAttribute("page-body", "shop.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    AuthToken authToken = authTokenDao.getTokenByBearer(token);
                    if (authToken != null) {
                        User user = userDao.getUserById(Long.parseLong(authToken.getSub()));
                        if (user != null) {
                            // Read the request body
                            String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(requestBody).getAsJsonObject();

                            // Extract characterId and characterCost from the JSON object
                            long characterId = jsonObject.getAsJsonPrimitive("characterId").getAsLong();
                            int characterCost = jsonObject.getAsJsonPrimitive("characterCost").getAsInt();

                            // Assuming you have a function to get the app context
                            String appContext = getAppContext(req);

                            // Your purchase logic here
                            // Update the database, deduct coins, add character to user, etc.
                            boolean purchaseSuccessful = userDao.purchaseCharacter( Long.parseLong( user.getId() ), characterId, characterCost );
                            if (purchaseSuccessful) {
                                // Redirect to the shop page after a successful purchase
                                resp.sendRedirect(appContext + "/shop");
                                return;
                            }
                        }
                    }
                    break;
                }
            }
        }

        // Handle the case where something went wrong with the purchase
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid purchase request");
    }

    private String getAppContext(HttpServletRequest req) {
        return req.getContextPath();
    }

}
