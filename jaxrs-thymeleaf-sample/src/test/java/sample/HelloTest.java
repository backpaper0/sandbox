package sample;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import sample.Hello.HelloResponse;

public class HelloTest {

    @Test
    public void test_sayHello() throws Exception {
        Message message = new Message();
        message.id = 1L;
        message.template = "Hello, %s!";
        when(messageDao.select(1L)).thenReturn(message);

        HelloResponse said = hello.sayHello("world");
        assertThat(said.getMessage(), is("Hello, world!"));

        verify(messageDao).select(1L);
    }
    private MessageDao messageDao;

    private Hello hello;

    @Before
    public void setUp() throws Exception {
        messageDao = mock(MessageDao.class);
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Hello.class);
                bind(MessageDao.class).toInstance(messageDao);
            }
        };
        Injector injector = Guice.createInjector(module);
        hello = injector.getInstance(Hello.class);
    }
}
