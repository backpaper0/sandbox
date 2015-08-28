package sample;

import org.seasar.doma.*;

@Dao
public interface SampleDao {
    @Select
    String select(int param);
}
