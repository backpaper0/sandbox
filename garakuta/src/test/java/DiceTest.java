import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiceTest {

	private final Dice dice = new Dice();

	@ParameterizedTest
	@MethodSource("parameters")
	void test(final String src, final String expected) throws Exception {
		final String actual = dice.solve(src);
		assertEquals(expected, actual);
	}

	static Stream<Arguments> parameters() {
		return Stream.of(
				/*0*/arguments("NNESWWS", "15635624"),
				/*1*/arguments("EEEE", "13641"),
				/*2*/arguments("WWWW", "14631"),
				/*3*/arguments("SSSS", "12651"),
				/*4*/arguments("NNNN", "15621"),
				/*5*/arguments("EENN", "13651"),
				/*6*/arguments("WWNN", "14651"),
				/*7*/arguments("SSNN", "12621"),
				/*8*/arguments("NENNN", "153641"),
				/*9*/arguments("NWNNN", "154631"),
				/*10*/arguments("SWWWSNEEEN", "12453635421"),
				/*11*/arguments("SENWSWSNSWE", "123123656545"),
				/*12*/arguments("SSSWNNNE", "126546315"),
				/*13*/arguments("SWNWSSSWWE", "12415423646"),
				/*14*/arguments("ENNWWS", "1354135"),
				/*15*/arguments("ESWNNW", "1321365"),
				/*16*/arguments("NWSSE", "154135"),
				/*17*/arguments("SWNWEWSEEN", "12415154135"),
				/*18*/arguments("EWNWEEEEWN", "13154532426"),
				/*19*/arguments("WNEWEWWWSNW", "145151562421"),
				/*20*/arguments("NNEE", "15631"),
				/*21*/arguments("EEEEWNWSW", "1364145642"),
				/*22*/arguments("SENNWWES", "123142321"),
				/*23*/arguments("SWWWSNSNESWW", "1245363635631"),
				/*24*/arguments("WESSENSE", "141263231"),
				/*25*/arguments("SWNSSESESSS", "124146231562"),
				/*26*/arguments("ENS", "1353"),
				/*27*/arguments("WNN", "1453"),
				/*28*/arguments("SSEENEEEN", "1263124536"),
				/*29*/arguments("NWSNNNW", "15414632"),
				/*30*/arguments("ESSSSSWW", "132453215"),
				/*31*/arguments("ESE", "1326"),
				/*32*/arguments("SNWNWWNSSSS", "121456232453"),
				/*33*/arguments("SWEESEN", "12423653"),
				/*34*/arguments("NEEWNSSWWW", "15323631562"),
				/*35*/arguments("WSEW", "14212"),
				/*36*/arguments("SWSNNNSNWE", "12464131353"),
				/*37*/arguments("ENWEWSEEW", "1351513545"),
				/*38*/arguments("WSEWN", "142124"),
				/*39*/arguments("EWNEESEWE", "1315321414"),
				/*40*/arguments("NESEEN", "1531263"),
				/*41*/arguments("WSW", "1426"),
				/*42*/arguments("ENEWE", "135656"));
	}
}
