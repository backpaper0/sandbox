package hentai.advent.calendar;

import org.junit.Test;

public class HentaiTest {

    @Test
    public void testHentai() throws Exception {
        JK jk = new JK();
        OmawaListener omawarisan = new OmawaListener();

        //おまわりさんが変態見たら通報してねと頼む
        jk.addObserver(omawarisan);

        //変　態　出　没
        jk.hentaiShutsubotsu();
    }

}
