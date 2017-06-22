package com.example;

import org.seasar.doma.*;

@Entity
public abstract class EntitySkeleton {
    @Column(name = "created_at")
    public java.time.LocalDateTime createdAt;
}
