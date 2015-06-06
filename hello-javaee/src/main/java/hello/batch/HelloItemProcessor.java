package hello.batch;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named
public class HelloItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        return IntStream.rangeClosed(0, (int) item).mapToObj(a -> "*")
                .collect(Collectors.joining());
    }
}
