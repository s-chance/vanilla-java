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
        List<Person> people = List.of(
                new Person("Peter", 33, "USA"),
                new Person("Brain", 10, "USA"),
                new Person("Jack", 12, "UK"),
                new Person("Alex", 22, "USA"),
                new Person("Steven", 24, "FR")
        );
//        long count = people.stream().count();
//        System.out.println(count);
//        Optional<Person> optionalPerson = people.stream().min(Comparator.comparingInt(Person::getAge));
//        optionalPerson.ifPresent(System.out::println);
//        IntStream ageStream = people.stream().mapToInt(Person::getAge);
//        int sum = ageStream.sum();
//        System.out.println(sum);
        IntStream ageStream = people.stream().mapToInt(Person::getAge);
        OptionalDouble average = ageStream.average();
        average.ifPresent(System.out::println);
    }
}