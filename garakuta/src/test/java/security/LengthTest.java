package security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import security.Length.Status;

public class LengthTest {

    @Test
    public void setBytes() throws Exception {
        final Length len = new Length(8);
        len.iValue = 0b10110011100011110000111110000011;
        final byte[] bs = new byte[10];
        len.writeTo(bs, 1);
        final byte[] expected =
            {
                0,
                0,
                0,
                0,
                0,
                (byte) 0b10110011,
                (byte) 0b10001111,
                (byte) 0b00001111,
                (byte) 0b10000011,
                0 };
        assertThat(bs, is(expected));
    }

    @Test
    public void increment() throws Exception {
        final Length len = new Length(8);
        len.iValue = Integer.MAX_VALUE - 9;

        assertThat(len.iValue, is(Integer.MAX_VALUE - 9));
        assertThat(len.lValue, is(0L));
        assertThat(len.status, is(Status.INT));

        len.increment();
        assertThat(len.iValue, is(Integer.MAX_VALUE - 1));
        assertThat(len.lValue, is(0L));
        assertThat(len.status, is(Status.INT));

        len.increment();
        assertThat(len.iValue, is(Integer.MAX_VALUE - 1));
        assertThat(len.lValue, is(Integer.MAX_VALUE + 7L));
        assertThat(len.status, is(Status.LONG));
    }
}
