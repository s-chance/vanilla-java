package org.entropy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream.Builder<String> streamBuilder = Stream.builder();
        streamBuilder.add("a");
        streamBuilder.add("b");
        if (Math.random() > 0.5) {
            streamBuilder.add("c");
        }
        Stream<String> stream = streamBuilder.build();
        stream.forEach(System.out::println);
    }
}