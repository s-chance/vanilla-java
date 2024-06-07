package org.entropy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream<String> stream1 = Stream.of("a", "b", "c");
        Stream<String> stream2 = Stream.of("d", "e", "f");
        Stream<String> concat = Stream.concat(stream1, stream2);
        concat.forEach(System.out::println);
    }
}