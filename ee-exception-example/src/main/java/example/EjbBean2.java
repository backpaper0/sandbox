package example;

import javax.ejb.Stateless;

@Stateless
public class EjbBean2 {

    public void exception() throws ExampleException {
        throw new ExampleException();
    }

    public void runtimeException() {
        throw new ExampleRuntimeException();
    }
}
