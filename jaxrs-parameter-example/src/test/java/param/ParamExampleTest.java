package param;

import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.ClassRule;
import org.junit.Test;
import test.JerseyTester;

/**
 * パラメータを受け取るテスト
 *
 */
public class ParamExampleTest {

    @Test
    public void Stringクラス() throws Exception {
        String out = jerseyTester.target("ParamExample").path("string")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("string: x"));
    }

    @Test
    public void プリミティブ() throws Exception {
        String out = jerseyTester.target("ParamExample").path("primitiveInt")
                .queryParam("in", "123").request().get(String.class);
        assertThat(out, is("primitiveInt: 123"));
    }

    @Test
    public void Stringの引数をひとつだけ受け取るコンストラクタ() throws Exception {
        String out = jerseyTester.target("ParamExample").path("constructor")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("constructor: x"));
    }

    @Test
    public void valueOfという名前のstaticファクトリメソッド() throws Exception {
        String out = jerseyTester.target("ParamExample").path("valueOf")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("valueOf: x"));
    }

    @Test
    public void fromStringという名前のstaticファクトリメソッド() throws Exception {
        String out = jerseyTester.target("ParamExample").path("fromString")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("fromString: x"));
    }

    @Test
    public void valueOfとfromStringの両方が定義されている場合はvalueOfが優先される()
            throws Exception {
        String out = jerseyTester.target("ParamExample").path(
                "valueOfAndFromString")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("valueOfAndFromString: x"));
    }

    @Test
    public void 列挙型はfromStringが優先される() throws Exception {
        String out = jerseyTester.target("ParamExample").path("enumFromString")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("enumFromString: x"));
    }

    @Test
    public void ParamConverterを利用する() throws Exception {
        String out = jerseyTester.target("ParamExample").path("converter")
                .queryParam("in", "x").request().get(String.class);
        assertThat(out, is("converter: x"));
    }
    @ClassRule
    public static JerseyTester jerseyTester = new JerseyTester(
            new JerseyTest() {
        @Override
        protected Application configure() {
            ResourceConfig config = new ResourceConfig();
            config.register(ParamExample.class);
            config.register(ParamConverterProviderImpl.class);
            return config;
        }
    });
}
