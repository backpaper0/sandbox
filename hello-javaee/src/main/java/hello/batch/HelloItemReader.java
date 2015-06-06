package hello.batch;

import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;

@Dependent
public class HelloItemReader extends AbstractItemReader {

    private int counter;

    @Override
    public Object readItem() throws Exception {
        if (counter < 100) {
            return counter++;
        }
        return null;
    }
}
