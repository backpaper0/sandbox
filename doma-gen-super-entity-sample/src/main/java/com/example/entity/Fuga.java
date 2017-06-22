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
@Entity(listener = FugaListener.class)
@Table(name = "FUGA")
public class Fuga extends EntitySkeleton {

    /**  */
    @Id
    @Column(name = "BAR")
    public Long bar;

    /**  */
    @Column(name = "BAZ")
    public String baz;
}