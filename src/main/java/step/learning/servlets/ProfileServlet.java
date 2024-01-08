package step.learning.servlets;

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
import java.util.Random;
import java.util.logging.Logger;

@Singleton
public class ProfileServlet extends HttpServlet {
    private UserDao userDao;
    private AuthTokenDao authTokenDao;
    private Logger logger;
    private CharacterDao characterDao;

    @Inject
    public ProfileServlet(UserDao userDao, AuthTokenDao authTokenDao, Logger logger, CharacterDao characterDao) {
        this.userDao = userDao;
        this.authTokenDao = authTokenDao;
        this.logger = logger;
        this.characterDao = characterDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    AuthToken authToken = authTokenDao.getTokenByBearer(token);
                    if(authToken != null) {
                        User user = userDao.getUserById(Long.parseLong(authToken.getSub()));
                        if(user != null) {
                            req.setAttribute("user-model", user);
                            List<Character> characters = characterDao.getAllOwnedBy( Long.parseLong( user.getId() ) );
                            req.setAttribute("characters", characters);
                        }
                    }
                    break;
                }
            }
        }

        req.setAttribute("page-body", "profile.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    AuthToken authToken = authTokenDao.getTokenByBearer(token);
                    if(authToken != null) {
                        User user = userDao.getUserById(Long.parseLong(authToken.getSub()));
                        if(user != null) {
                            int randomCoins = new Random().nextInt(10000) + 1;
                            int newCoins = user.getCoins() + randomCoins;
                            if (userDao.updateUserCoins(Long.parseLong(user.getId()), newCoins)) {
                                // Send a success response
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("Coins added successfully");
                            } else {
                                // Send an error response
                                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                resp.getWriter().write("Failed to update user coins");
                            }
                        } else {
                            // Send an unauthorized response
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            resp.getWriter().write("Unauthorized access");
                        }
                    }
                    break;
                }
            }
        }
    }
}