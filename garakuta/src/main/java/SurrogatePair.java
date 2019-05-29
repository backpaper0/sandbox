import java.text.BreakIterator;

public interface SurrogatePair {

    public static void main(final String[] args) {
        final String s = "𩸽を食べたい";
        System.out.println(s.length());
        System.out.println(s.codePointCount(0, s.length()));
        System.out.println(s.codePoints().count());

        final BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(s);

        int count = 0;
        while (it.next() != BreakIterator.DONE) {
            count++;
        }
        System.out.println(count);
    }
}