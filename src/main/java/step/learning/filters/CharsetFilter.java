package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter {
    private FilterConfig filterConfig;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    public void doFilter(                                       // Основний метод, якмй викликається Servlet-API
            ServletRequest servletRequest,                      // об'єкти запиту та відповіді, але !!!
            ServletResponse servletResponse,                    // приходять як узагальнені (не HTTP)
            FilterChain filterChain)                            // Ланцюг фільтрів для продовження
    throws IOException, ServletException {
        // Прямий хід
        // Через те що вхідні параметри мають узагальнений тип,
        // для певних задач їх треба явно перетворити
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        // з цього моменту це ті ж об'єкти, що й у сервлетах
        String charsetName = StandardCharsets.UTF_8.name();
        req.setCharacterEncoding(charsetName);
        res.setCharacterEncoding(charsetName);
        // для передачі даних далі по ланці використовуємо атрибути
        req.setAttribute("charsetName", charsetName);
        // Передача роботи по ланцюгу
        filterChain.doFilter(servletRequest, servletResponse);

        // Зворотній хід - етап надсилання відповіді

    }
    public void destroy() {
        this.filterConfig = null;
    }
}
