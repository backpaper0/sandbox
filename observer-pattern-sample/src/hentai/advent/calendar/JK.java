package hentai.advent.calendar;

/**
 * jk
 *
 */
public class JK extends Subject {

    public void hentaiShutsubotsu() {
        System.out.println("jk<ｷｬｰ!!ﾍﾝﾀｲｰ!!");
        HentaEvent event = new HentaEvent(this);
        notifyObservers(event);
    }

    @Override
    public String toString() {
        return "jk";
    }

}
