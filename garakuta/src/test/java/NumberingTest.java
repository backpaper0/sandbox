import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NumberingTest extends Numbering {

    @Test
    void test() {
        final Numbering n = new Numbering();
        assertEquals("## <a name=\"no1\">1.foo</a>", n.convert("## foo"));
        assertEquals("### <a name=\"no1-1\">1.1.bar</a>", n.convert("### bar"));
        assertEquals("### <a name=\"no1-2\">1.2.baz</a>", n.convert("### baz"));
        assertEquals("## <a name=\"no2\">2.qux</a>", n.convert("## qux"));
        assertEquals("## <a name=\"no3\">3.hoge</a>", n.convert("## 4.hoge"));
        assertEquals("### <a name=\"no3-1\">3.1.fuga</a>", n.convert("### fuga"));
        assertEquals("### <a name=\"no3-2\">3.2.piyo</a>",
                n.convert("### <a name=\"no4-10\">piyo</a>"));
    }

}
