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
                        new Person("Alex", 22, "USA")
                ),
                List.of(
                        new Person("Steven", 24, "FR")
                )
        );
        List<Person> people = List.of(
                new Person("Peter", 33, "USA"),
                new Person("Brain", 10, "USA"),
                new Person("Jack", 12, "UK"),
                new Person("Alex", 22, "USA"),
                new Person("Steven", 24, "FR")
        );
        Stream.of("blueberry", "strawberry", "apple", "peach", "pear")
                .sorted(Comparator.comparingInt(String::length).reversed())
                .forEach(System.out::println);
        people.stream()
                .sorted(Comparator.comparingInt(Person::getAge).reversed())
                .forEach(System.out::println);
    }
}