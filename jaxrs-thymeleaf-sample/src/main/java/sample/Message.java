package sample;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Message {

    @Id
    public Long id;

    public String template;

}
