package sample;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.seasar.extension.jdbc.JdbcManager;

@Transactional
@RequestScoped
@Path("hello")
public class HelloWorld {

    @Inject
    private JdbcManager jdbcManager;

    /*
     * JdbcManagerでMessageテーブルからメッセージテンプレートを
     * 取ってきてString.formatして返すリソースメソッド。
     * 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {

        Message message = jdbcManager.from(Message.class).id("hello")
                .getSingleResult();

        return String.format(message.template, "world");
    }
}
