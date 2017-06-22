package com.example;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.*;

public abstract class EntityListenerSkeleton<T extends EntitySkeleton> implements EntityListener<T> {
    @Override
    public void preInsert(T entity, PreInsertContext<T> context) {
        entity.createdAt = java.time.LocalDateTime.now();
    }
}
