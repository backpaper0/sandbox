package hentai.advent.calendar;

import org.junit.jupiter.api.Test;

class HentaiTest {

    @Test
    void testHentai() throws Exception {
        final JK jk = new JK();
        final OmawaListener omawarisan = new OmawaListener();

        //おまわりさんが変態見たら通報してねと頼む
        jk.addObserver(omawarisan);

        //変　態　出　没
        jk.hentaiShutsubotsu();
    }

}
