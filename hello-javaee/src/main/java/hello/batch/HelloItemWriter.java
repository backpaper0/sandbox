package hello.batch;

import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;

@Dependent
public class HelloItemWriter extends AbstractItemWriter {

    private static final Logger logger = Logger.getLogger(HelloItemWriter.class
            .getName());

    @Override
    public void writeItems(List<Object> items) throws Exception {
        logger.info(() -> String.valueOf(items));
    }
}
