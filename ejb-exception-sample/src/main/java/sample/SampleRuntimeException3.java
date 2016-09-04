package sample;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SampleRuntimeException3 extends RuntimeException {
}
