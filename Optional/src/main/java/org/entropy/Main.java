package org.entropy;

import java.util.Optional;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        Optional<User> optionalUser = userRepository.findUserByName("entropy2");

//        Optional<Optional<String>> s = optionalUser.map(User::getFullName2);
//        Optional<String> s2 = optionalUser.flatMap(User::getFullName2);

        Stream<String> a = optionalUser
                .map(User::getName)
                .stream();
        a.forEach(System.out::println);

//        optionalUser.ifPresent(user -> System.out.println(user.getFullName()));
//        optionalUser.ifPresentOrElse(
//                user -> System.out.println(user.getFullName()),
//                () -> System.out.println("User not found")
//        );

//        Optional<User> optionalUser2 = optionalUser.filter(user -> user.getFullName().equals("entropy tree"));
//        System.out.println(optionalUser2.isPresent());
//
//        Optional<String> optionalFullName = optionalUser.map(User::getFullName);
//        System.out.println(optionalFullName.isPresent());


        // why is this code doing the same thing again?
        // that's not what it was meant to be, and it shouldn't be used that way
//        if (optionalUser.isPresent()) {
//            System.out.println(optionalUser.get().getFullName());
//        } else {
//            User defaultUser = new User("default", "default user");
//            System.out.println(defaultUser.getFullName());
//        }

        // User will instantiate whether they are null or not
//        User user = optionalUser.orElse(new User("default", "default user"));
//        System.out.println(user.getFullName());

        // User will instantiate only when the optionalUser is null
//        User user2 = optionalUser.orElseGet(() -> new User("default", "default user"));
//        System.out.println(user2.getFullName());

        // throws a custom exception
//        optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        // if you need to process a large number of null types, that will be a tedious work
//        if (user != null) {
//            System.out.println(user.getFullName());
//        } else {
//            User defaultUser = new User("default", "default user");
//            System.out.println(defaultUser.getFullName());
//        }

//        String value = "entropy";
//        Optional<String> optionalBox = Optional.ofNullable(value);
//        System.out.println(optionalBox.get()); // not recommend
//        System.out.println(optionalBox.isPresent());
//        System.out.println(optionalBox.isEmpty());

    }
}
