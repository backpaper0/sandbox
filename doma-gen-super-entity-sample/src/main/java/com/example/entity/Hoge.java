package com.example.entity;

import com.example.EntitySkeleton;
import java.time.LocalDateTime;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * 
 */
@Entity(listener = HogeListener.class)
@Table(name = "HOGE")
public class Hoge extends EntitySkeleton {

    /**  */
    @Id
    @Column(name = "FOO")
    public Long foo;
}