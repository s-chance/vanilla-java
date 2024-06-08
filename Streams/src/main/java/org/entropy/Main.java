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
        Integer res = List.of(1, 2, 3, 4).parallelStream()
                .reduce(0, (a, b) -> {
                    System.out.println(a + " - " + b + " Thread: " + Thread.currentThread().getName());
                    return a - b;
                });
        System.out.println(res);
    }
}