package hentai.advent.calendar.java.util;

import java.util.Observable;
import java.util.Observer;

/**
 * おまわりさん
 *
 */
public class OmawaListener implements Observer {

    @Override
    public void update(Observable o, Object event) {
        System.out.printf("おまわりさん<%sから通報ktkr%n", o);
        if (event instanceof HentaEvent) {
            System.out.println("変態<ﾀｲ━━━━||Φ|(|ﾟ|∀|ﾟ|)|Φ||━━━━ﾎ!!");
        }
    }

}
