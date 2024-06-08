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
        List<Person> people = List.of(
                new Person("Peter", 33, "USA"),
                new Person("Brain", 10, "USA"),
                new Person("Jack", 12, "UK"),
                new Person("Alex", 22, "USA"),
                new Person("Steven", 24, "FR")
        );
//        ArrayList<Person> collect = people.stream().parallel()
//                .collect(Collector.of(
//                        () -> new ArrayList<>(),
//                        (list, person) -> {
//                            System.out.println("Accumulator: " + person);
//                            list.add(person);
//                        },
//                        (left, right) -> {
//                            System.out.println("Combiner: " + left);
//                            left.addAll(right);
//                            return left;
//                        },
//                        Collector.Characteristics.IDENTITY_FINISH
//                ));
//        System.out.println(collect);
        int size = people.stream().parallel()
                .collect(Collector.of(
                        HashMap<String, List<Person>>::new,
                        (map, person) -> {
                            System.out.println("Accumulator: " + person);
                            map.computeIfAbsent(person.getCountry(), k -> new ArrayList<>()).add(person);
                        },
                        (left, right) -> {
                            System.out.println("Combiner: "
                                    + System.lineSeparator() + "left: " + left
                                    + System.lineSeparator() + "right: " + right
                            );
                            right.forEach((k, v) -> left.merge(k, v, (list, newList) -> {
                                list.addAll(newList);
                                return list;
                            }));
                            return left;
                        },
                        HashMap::size
                ));
        System.out.println(size);
    }
}