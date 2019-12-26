import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BitBombermanTest {

    private final BitBomberman bitBomberman = new BitBomberman();

    @ParameterizedTest
    @MethodSource("parameters")
    void test(final String src, final String expected) throws Exception {
        final String actual = bitBomberman.bomb(src);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> parameters() {
        return Stream.of(
                /*0*/arguments("802b1200/01400c20", "53c40cfc"),
                /*1*/arguments("28301068/84080504", "d64fef94"),
                /*2*/arguments("100a4010/80010004", "e241850c"),
                /*3*/arguments("81020400/000000fc", "0e3cfbfc"),
                /*4*/arguments("80225020/7e082080", "7fdd24d0"),
                /*5*/arguments("01201200/40102008", "fe1861fc"),
                /*6*/arguments("00201000/01000200", "43c48f08"),
                /*7*/arguments("00891220/81020408", "ff060c1c"),
                /*8*/arguments("410033c0/0c300000", "3cf0c000"),
                /*9*/arguments("00000000/01400a00", "7bf7bf78"),
                /*10*/arguments("00000000/20000a00", "fca2bf28"),
                /*11*/arguments("00000000/00000000", "00000000"),
                /*12*/arguments("00cafe00/00000000", "00000000"),
                /*13*/arguments("aaabaaaa/50000000", "51441040"),
                /*14*/arguments("a95a95a8/56a56a54", "56a56a54"),
                /*15*/arguments("104fc820/80201010", "ea30345c"),
                /*16*/arguments("4a940214/05000008", "05000008"),
                /*17*/arguments("00908000/05000200", "ff043f48"),
                /*18*/arguments("00c48c00/fe1861fc", "ff3873fc"),
                /*19*/arguments("00000004/81020400", "fffffff0"),
                /*20*/arguments("111028b0/40021100", "e08fd744"),
                /*21*/arguments("6808490c/01959000", "17f7b650"),
                /*22*/arguments("30821004/81014040", "c75de5f8"),
                /*23*/arguments("0004c810/10003100", "fe4937c4"),
                /*24*/arguments("12022020/88200000", "edf08208"),
                /*25*/arguments("2aa92098/01160000", "45165964"),
                /*26*/arguments("00242940/10010004", "fc43c43c"),
                /*27*/arguments("483c2120/11004c00", "33c3de10"),
                /*28*/arguments("10140140/44004a04", "eda3fe3c"),
                /*29*/arguments("0c901d38/72602200", "f36da280"));
    }
}
