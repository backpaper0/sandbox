package sample

import org.seasar.doma.*

Dao
public interface UserDao {

    @Select
    fun select(param: Int): String

}
