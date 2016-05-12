package sample;

import javax.persistence.*;

@Entity
public class Sample implements java.io.Serializable {
    private Integer id;
    private String text;
    @Id
    @GeneratedValue
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    @Override
    public String toString() { return String.format("%1$s:%2$s", id, text); }
}
