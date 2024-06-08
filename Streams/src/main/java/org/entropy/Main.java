package org.entropy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> collect = Set.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
                .map(String::toLowerCase)
                .collect(Collector.of(
                        () -> {
                            System.out.println("Supplier: new ConcurrentHashMap" + " Thread: " + Thread.currentThread().getName());
                            return new ConcurrentHashMap<>();
                        },
                        (map, item) -> {
                            System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
                            map.put(item.toUpperCase(), item);
                        },
                        (left, right) -> {
                            System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
                            left.putAll(right);
                            return left;
                        },
                        Collector.Characteristics.IDENTITY_FINISH,
                        Collector.Characteristics.CONCURRENT
                ));
        System.out.println(collect);
    }
}