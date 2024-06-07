package org.entropy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String[] array = {"a", "b", "c"};
        Stream<String> stream = Arrays.stream(array);
        stream.forEach(System.out::println);
    }
}