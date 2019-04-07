package hentai.advent.calendar.java.util;

import org.junit.jupiter.api.Test;

class HentaiTest {

    @Test
    void testHentai() throws Exception {
        final JK jk = new JK();
        final OmawaListener omawarisan = new OmawaListener();
        jk.addObserver(omawarisan);
        jk.hentaiShutsubotsu();
    }

}
