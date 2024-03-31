package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private LockMode lockMode = LockMode.FOR_UPDATE_SKIP_LOCKED;
    private long timeout = 10;

    public LockMode getLockMode() {
        return lockMode;
    }

    public void setLockMode(LockMode lockMode) {
        this.lockMode = lockMode;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public enum LockMode {

        FOR_UPDATE("for update"),
        FOR_UPDATE_SKIP_LOCKED("for update skip locked");

        private final String sql;

        private LockMode(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }
}
