package sample.testcase;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sample.test.Depends;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FugaTest {

    @Rule
    public Depends depends = new Depends();

    @Test
    public void testBar() throws Exception {
    }
}
