import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DiceTest {
    private final String src;
    private final String expected;
    private final Dice dice = new Dice();

    public DiceTest(int disuse, String src, String expected) {
        this.src = src;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        String actual = dice.solve(src);
        assertThat(actual, is(expected));
    }

    @Parameters(name = "{0}: {1} -> {2}")
    public static Collection<Object[]> parameters() {
        Collection<Object[]> ps = new ArrayList<>();
        BiConsumer<String, String> fn = (src, expected) -> ps.add(new Object[] {
                ps.size(), src, expected });
        /*0*/fn.accept("NNESWWS", "15635624");
        /*1*/fn.accept("EEEE", "13641");
        /*2*/fn.accept("WWWW", "14631");
        /*3*/fn.accept("SSSS", "12651");
        /*4*/fn.accept("NNNN", "15621");
        /*5*/fn.accept("EENN", "13651");
        /*6*/fn.accept("WWNN", "14651");
        /*7*/fn.accept("SSNN", "12621");
        /*8*/fn.accept("NENNN", "153641");
        /*9*/fn.accept("NWNNN", "154631");
        /*10*/fn.accept("SWWWSNEEEN", "12453635421");
        /*11*/fn.accept("SENWSWSNSWE", "123123656545");
        /*12*/fn.accept("SSSWNNNE", "126546315");
        /*13*/fn.accept("SWNWSSSWWE", "12415423646");
        /*14*/fn.accept("ENNWWS", "1354135");
        /*15*/fn.accept("ESWNNW", "1321365");
        /*16*/fn.accept("NWSSE", "154135");
        /*17*/fn.accept("SWNWEWSEEN", "12415154135");
        /*18*/fn.accept("EWNWEEEEWN", "13154532426");
        /*19*/fn.accept("WNEWEWWWSNW", "145151562421");
        /*20*/fn.accept("NNEE", "15631");
        /*21*/fn.accept("EEEEWNWSW", "1364145642");
        /*22*/fn.accept("SENNWWES", "123142321");
        /*23*/fn.accept("SWWWSNSNESWW", "1245363635631");
        /*24*/fn.accept("WESSENSE", "141263231");
        /*25*/fn.accept("SWNSSESESSS", "124146231562");
        /*26*/fn.accept("ENS", "1353");
        /*27*/fn.accept("WNN", "1453");
        /*28*/fn.accept("SSEENEEEN", "1263124536");
        /*29*/fn.accept("NWSNNNW", "15414632");
        /*30*/fn.accept("ESSSSSWW", "132453215");
        /*31*/fn.accept("ESE", "1326");
        /*32*/fn.accept("SNWNWWNSSSS", "121456232453");
        /*33*/fn.accept("SWEESEN", "12423653");
        /*34*/fn.accept("NEEWNSSWWW", "15323631562");
        /*35*/fn.accept("WSEW", "14212");
        /*36*/fn.accept("SWSNNNSNWE", "12464131353");
        /*37*/fn.accept("ENWEWSEEW", "1351513545");
        /*38*/fn.accept("WSEWN", "142124");
        /*39*/fn.accept("EWNEESEWE", "1315321414");
        /*40*/fn.accept("NESEEN", "1531263");
        /*41*/fn.accept("WSW", "1426");
        /*42*/fn.accept("ENEWE", "135656");
        return ps;
    }
}
