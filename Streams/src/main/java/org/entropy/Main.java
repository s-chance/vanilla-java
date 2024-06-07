package org.entropy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Peter", 33, "USA"),
                new Person("Brain", 10, "USA"),
                new Person("Jack", 12, "UK"),
                new Person("Alex", 22, "USA"),
                new Person("Steven", 24, "FR")
        );
        List<Person> adults = new ArrayList<>();
        for (Person person : people) {
            if (person.getAge() > 18) {
                adults.add(person);
            }
        }
        System.out.println(adults);
    }
}
