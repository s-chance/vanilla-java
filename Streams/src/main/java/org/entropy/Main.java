package org.entropy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("a", "b", "c");
        Stream<String> stream = list.stream();
        stream.forEach(System.out::println);
    }
}