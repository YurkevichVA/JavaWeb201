package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // третій спосіб конфігурування фільтрів та сервлетів - IoC
        filter("/*").through(CharsetFilter.class);

        serve("/"       ).with(HomeServlet.class);
        serve("/admin"  ).with(AdminServlet.class);
        serve("/shop"   ).with(ShopServlet.class);
        serve("/profile").with(ProfileServlet.class);
        serve("/login"  ).with(LoginServlet.class);
        serve("/signup" ).with(SignupServlet.class);
        serve("/auth"   ).with(AuthServlet.class);



        //serve("/db"     ).with(DbServlet.class);
        //serve("/jsp"    ).with(JspServlet.class);
        //serve("/filters").with(FiltersServlet.class);
        //serve("/ioc"    ).with(IocServlet.class);
        //serve("/spa"    ).with(SpaServlet.class);
        //serve("/tpl/*"  ).with(TemplatesServlet.class);
    }//
}
