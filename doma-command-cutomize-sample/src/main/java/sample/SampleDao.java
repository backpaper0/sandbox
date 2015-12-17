package sample;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao
public interface SampleDao {

    @Script
    void createTable();

    @BatchInsert
    int[] insert(List<Sample> entities);

    @Select(strategy = SelectType.STREAM)
    <R> R select(Function<Stream<Sample>, R> fn);
}
