package sample;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue
    public Long id;

    public String name;
}
