package com.example;

import org.seasar.doma.*;
import java.util.*;
import com.example.entity.*;

@Dao
public interface SampleDao {
    @Insert
    int insertHoge(Hoge hoge);
    @Insert
    int insertFuga(Fuga fuga);
    @Select
    List<Hoge> selectAllHoge();
    @Select
    List<Fuga> selectAllFuga();
}

