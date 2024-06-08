package org.entropy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<List<Person>> peopleGroups = List.of(
                List.of(
                        new Person("Peter", 33, "USA"),
                        new Person("Brain", 10, "USA")
                ),
                List.of(
                        new Person("Jack", 12, "UK"),
                        new Person("Alex", 22, "USA"),
                        new Person("Alex", 22, "USA")
                ),
                List.of(
                        new Person("Steven", 24, "FR"),
                        new Person("Steven", 24, "FR")
                )
        );
        Stream<String> results = peopleGroups.stream()
                .flatMap(Collection::stream)
                .filter(person -> person.getAge() > 18)
                .distinct()
                .sorted(Comparator.comparingInt(Person::getAge).reversed())
                .map(Person::getName)
                .limit(3)
                .skip(1);
        results.forEach(System.out::println);
    }
}