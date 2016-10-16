package sample.dao;

import java.util.List;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;

import sample.config.SampleConfig;
import sample.entity.Message;

@Dao(config = SampleConfig.class)
public interface MessageDao {
    @Script
    void createTable();
    @Insert
    int insert(Message entity);
    @BatchInsert
    int[] insert(List<Message> entities);
}
