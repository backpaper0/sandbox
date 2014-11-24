package hentai.advent.calendar;

/**
 * おまわりさん
 *
 */
public class OmawaListener implements Observer {

    @Override
    public void notify(Event event) {
        System.out.printf("おまわりさん<%sから通報ktkr%n", event.getSource());
        if (event instanceof HentaEvent) {
            System.out.println("変態<ﾀｲ━━━━||Φ|(|ﾟ|∀|ﾟ|)|Φ||━━━━ﾎ!!");
        }
    }

}
