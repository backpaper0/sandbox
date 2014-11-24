package sample;

import javax.inject.Inject;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
@AnnotateWith(annotations = {
    @Annotation(type = Inject.class, target = AnnotationTarget.CONSTRUCTOR)
})
public interface MessageDao {

    @Select
    Message select(Long id);
}
