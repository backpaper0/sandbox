package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.seasar.doma.jdbc.tx.TransactionManager;

import sample.config.SampleConfig;
import sample.dao.MessageDao;
import sample.dao.MessageDaoImpl;
import sample.entity.Message;

public class BatchInsertSample {

    public static void main(String[] args) {

        MessageDao dao = new MessageDaoImpl();

        TransactionManager tx = SampleConfig.singleton().getTransactionManager();

        tx.required(() -> {
            dao.createTable();
        });

        tx.required(() -> {
            Message msg = new Message();
            msg.text = "hoge";
            dao.insert(msg);
            Objects.requireNonNull(msg.id);

            System.out.println(msg.id);
        });

        tx.required(() -> {
            List<Message> msgs = new ArrayList<>();

            Message msg1 = new Message();
            msg1.text = "foo";
            msgs.add(msg1);

            Message msg2 = new Message();
            msg2.text = "bar";
            msgs.add(msg2);

            dao.insert(msgs);
            Objects.requireNonNull(msg1.id);
            Objects.requireNonNull(msg2.id);

            System.out.println(msg1.id);
            System.out.println(msg2.id);
        });
    }
}
