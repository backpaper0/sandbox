package org.sample;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ListOperations {

    @Param
    private ListImplementation listImplementation;

    private static List<Integer> addMe1 = IntStream.rangeClosed(1, 100)
            .mapToObj(Integer::valueOf)
            .collect(toList());
    private static Integer addMe2 = 0;
    private static int size = addMe1.size() / 2;
    private static Integer findMe = addMe1.get(size);
    private List<Integer> list1;
    private List<Integer> list2;

    @Setup(Level.Invocation)
    public void setUp() {
        list1 = listImplementation.get();
        list2 = listImplementation.get();
        list2.addAll(addMe1);
    }

    @Benchmark
    public void get() {
        list2.get(size);
    }

    @Benchmark
    public void iteration() {
        for (final Integer unused : list2) {
        }
    }

    @Benchmark
    public void contains() {
        list2.contains(findMe);
    }

    @Benchmark
    public void indexOf() {
        list2.indexOf(findMe);
    }

    @Benchmark
    public void add() {
        list1.add(addMe2);
    }

    @Benchmark
    public void addAll() {
        list1.addAll(addMe1);
    }

    public enum ListImplementation {
        ArrayList(java.util.ArrayList::new),
        LinkedList(java.util.LinkedList::new),
        CopyOnWriteArrayList(java.util.concurrent.CopyOnWriteArrayList::new),
        SynchronizedArrayList(() -> Collections.synchronizedList(new java.util.ArrayList<>()));

        private final Supplier<List<Integer>> factory;

        private ListImplementation(final Supplier<List<Integer>> factory) {
            this.factory = factory;
        }

        public List<Integer> get() {
            return factory.get();
        }
    }
}
