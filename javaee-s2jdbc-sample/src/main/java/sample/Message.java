package sample;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {

    @Id
    public String id;

    public String template;
}
