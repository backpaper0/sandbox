import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class ThreadDump {

    public static void main(final String[] args) {
        Arrays.stream(
                ManagementFactory.getThreadMXBean().dumpAllThreads(true, true))
                .forEach(System.out::println);
    }
}
