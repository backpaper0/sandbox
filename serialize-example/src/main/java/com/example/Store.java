package com.example;

import java.io.IOException;

public interface Store {

    byte[] writeToBytes(Object obj) throws IOException;

    Object readFromBytes(byte[] b) throws IOException, ClassNotFoundException;

    static Store getInstance(final ClassLoader classLoader)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Store) classLoader.loadClass("com.example.DefaultStore")
                .newInstance();
    }

    static Object writeAndRead(final Store w, final Store r, final Object obj)
            throws ClassNotFoundException, IOException {
        final byte[] b = w.writeToBytes(obj);
        return r.readFromBytes(b);
    }
}
