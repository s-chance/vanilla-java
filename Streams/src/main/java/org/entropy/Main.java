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
//        List<Person> adults = people.stream()
//                .filter(person -> person.getAge() > 18)
//                .collect(Collectors.toList());
//        System.out.println(adults);
//        Map<String, Integer> adults = people.stream()
//                .filter(person -> person.getAge() > 18)
//                .collect(Collectors.toMap(
//                        Person::getName,
//                        Person::getAge
//                ));
//        System.out.println(adults);
        Map<String, List<Person>> peopleByCountry = people.stream()
                .collect(Collectors.groupingBy(Person::getCountry));
        peopleByCountry.forEach((k, v) -> System.out.println(k + "=" + v));

        Map<Boolean, List<Person>> agePartition = people.stream()
                .collect(Collectors.groupingBy(person -> person.getAge() > 18));
        agePartition.forEach((k, v) -> System.out.println(k + "=" + v));

        String joinedName = people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(","));
        System.out.println(joinedName);

        IntSummaryStatistics ageSummary = people.stream()
                .collect(Collectors.summarizingInt(Person::getAge));
        System.out.println(ageSummary);
        System.out.println(ageSummary.getAverage());
        System.out.println(ageSummary.getMax());
    }
}