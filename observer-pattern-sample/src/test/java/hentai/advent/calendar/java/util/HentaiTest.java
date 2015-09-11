package hentai.advent.calendar.java.util;

import org.junit.Test;

public class HentaiTest {

    @Test
    public void testHentai() throws Exception {
        JK jk = new JK();
        OmawaListener omawarisan = new OmawaListener();
        jk.addObserver(omawarisan);
        jk.hentaiShutsubotsu();
    }

}
