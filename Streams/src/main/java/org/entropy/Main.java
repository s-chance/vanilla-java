package org.entropy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
                .map(String::toLowerCase)
                .forEach(item -> {
                    System.out.println("Item: " + item + " -> " + " Thread: " + Thread.currentThread().getName());
                    System.out.println(item);
                });
    }
}