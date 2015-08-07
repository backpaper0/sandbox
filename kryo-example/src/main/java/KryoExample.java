import java.io.ByteArrayOutputStream;
import java.time.MonthDay;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoExample {

    public static void main(String[] args) {
        Person p = new Person();
        p.name = "うらがみ";
        p.birthday = MonthDay.of(2, 10);
        p.height = 180;

        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(MonthDay.class, MonthDaySerializer.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Output output = new Output(out)) {
            kryo.writeObject(output, p);
        }

        byte[] bs = out.toByteArray();
        for (byte b : bs) {
            System.out.printf("%02x", b & 0xff);
        }
        System.out.println();

        Person p2;
        try (Input input = new Input(bs)) {
            p2 = kryo.readObject(input, Person.class);
        }

        System.out.println(p2);
    }

    public static class Person {
        public String name;
        public MonthDay birthday;
        public int height;

        @Override
        public String toString() {
            return String.format("name:%s brithday:%s height:%s", name,
                    birthday, height);
        }
    }

    public static class MonthDaySerializer extends Serializer<MonthDay> {

        @Override
        public void write(Kryo kryo, Output output, MonthDay object) {
            output.writeInt(object.getMonthValue());
            output.writeInt(object.getDayOfMonth());
        }

        @Override
        public MonthDay read(Kryo kryo, Input input, Class<MonthDay> type) {
            return MonthDay.of(input.readInt(), input.readInt());
        }
    }
}
