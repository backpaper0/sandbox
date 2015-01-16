import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BitBombermanTest {

    private final String src;
    private final String expected;
    private final BitBomberman bitBomberman = new BitBomberman();

    public BitBombermanTest(int disuse, String src, String expected) {
        this.src = src;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        String actual = bitBomberman.bomb(src);
        assertThat(actual, is(expected));
    }

    @Parameters(name = "{0}: {1} -> {2}")
    public static Collection<Object[]> parameters() {

        List<Object[]> ps = new ArrayList<>();

        BiConsumer<String, String> fn = (src, expected) -> {
            ps.add(new Object[] { ps.size(), src, expected });
        };

        /*0*/fn.accept("802b1200/01400c20", "53c40cfc");
        /*1*/fn.accept("28301068/84080504", "d64fef94");
        /*2*/fn.accept("100a4010/80010004", "e241850c");
        /*3*/fn.accept("81020400/000000fc", "0e3cfbfc");
        /*4*/fn.accept("80225020/7e082080", "7fdd24d0");
        /*5*/fn.accept("01201200/40102008", "fe1861fc");
        /*6*/fn.accept("00201000/01000200", "43c48f08");
        /*7*/fn.accept("00891220/81020408", "ff060c1c");
        /*8*/fn.accept("410033c0/0c300000", "3cf0c000");
        /*9*/fn.accept("00000000/01400a00", "7bf7bf78");
        /*10*/fn.accept("00000000/20000a00", "fca2bf28");
        /*11*/fn.accept("00000000/00000000", "00000000");
        /*12*/fn.accept("00cafe00/00000000", "00000000");
        /*13*/fn.accept("aaabaaaa/50000000", "51441040");
        /*14*/fn.accept("a95a95a8/56a56a54", "56a56a54");
        /*15*/fn.accept("104fc820/80201010", "ea30345c");
        /*16*/fn.accept("4a940214/05000008", "05000008");
        /*17*/fn.accept("00908000/05000200", "ff043f48");
        /*18*/fn.accept("00c48c00/fe1861fc", "ff3873fc");
        /*19*/fn.accept("00000004/81020400", "fffffff0");
        /*20*/fn.accept("111028b0/40021100", "e08fd744");
        /*21*/fn.accept("6808490c/01959000", "17f7b650");
        /*22*/fn.accept("30821004/81014040", "c75de5f8");
        /*23*/fn.accept("0004c810/10003100", "fe4937c4");
        /*24*/fn.accept("12022020/88200000", "edf08208");
        /*25*/fn.accept("2aa92098/01160000", "45165964");
        /*26*/fn.accept("00242940/10010004", "fc43c43c");
        /*27*/fn.accept("483c2120/11004c00", "33c3de10");
        /*28*/fn.accept("10140140/44004a04", "eda3fe3c");
        /*29*/fn.accept("0c901d38/72602200", "f36da280");

        return ps;
    }
}
