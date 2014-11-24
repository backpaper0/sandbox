package hentai.advent.calendar.java.util;

import java.util.Observable;

/**
 * jk
 *
 */
public class JK extends Observable {

    public void hentaiShutsubotsu() {
        System.out.println("jk<ｷｬｰ!!ﾍﾝﾀｲｰ!!");
        HentaEvent event = new HentaEvent(this);
        setChanged();
        notifyObservers(event);
    }

    @Override
    public String toString() {
        return "jk";
    }
}
