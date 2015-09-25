package example;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ExampleEntity.class)
public class ExampleEntity_ {

    public static SingularAttribute<ExampleEntity, Integer> id;
    public static SingularAttribute<ExampleEntity, Integer> version;
}
