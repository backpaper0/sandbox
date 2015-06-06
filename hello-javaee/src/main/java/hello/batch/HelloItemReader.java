package hello.batch;

import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named
public class HelloItemReader extends AbstractItemReader {

    private int counter;

    @Override
    public Object readItem() throws Exception {
        if (counter < 20) {
            return counter++;
        }
        return null;
    }
}
