package com.example;

public class CommitterListener {

    public void handlePrePersist(Committer committer) {
        committer.setVersion(0);
    }

    public void handlePreUpdate(Committer committer) {
        committer.setVersion(committer.getVersion() + 1);
    }
}
