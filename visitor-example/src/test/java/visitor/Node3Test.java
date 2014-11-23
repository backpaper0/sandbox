package visitor;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.Test;

public class Node3Test {

    @Test public void testCalc() throws Exception {
        Node3 node1 = new NumNode3(2);
        Node3 node2 = new NumNode3(3);
        Node3 node3 = new AddNode3(node1, node2);
        Node3 node4 = new NumNode3(4);
        Node3 node5 = new AddNode3(node3, node4);
        Calclurator3 calclurator = new Calclurator3();
        int actual = calclurator.calc(node5);
        int expected = 2 + 3 + 4;
        assertThat(actual, is(expected));
    }
}
