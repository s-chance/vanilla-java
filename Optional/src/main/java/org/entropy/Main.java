package org.entropy;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        Optional<User> optionalUser = userRepository.findUserByName("entropy2");

        // why is this code doing the same thing again?
        // that's not what it was meant to be, and it shouldn't be used that way
//        if (optionalUser.isPresent()) {
//            System.out.println(optionalUser.get().getFullName());
//        } else {
//            User defaultUser = new User("default", "default user");
//            System.out.println(defaultUser.getFullName());
//        }

        // User will instantiate whether they are null or not
        User user = optionalUser.orElse(new User("default", "default user"));
        System.out.println(user.getFullName());

        // User will instantiate only when the optionalUser is null
        User user2 = optionalUser.orElseGet(() -> new User("default", "default user"));
        System.out.println(user2.getFullName());

        // throws a custom exception
//        optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        // if you need to process a large number of null types, that will be a tedious work
//        if (user != null) {
//            System.out.println(user.getFullName());
//        } else {
//            User defaultUser = new User("default", "default user");
//            System.out.println(defaultUser.getFullName());
//        }

        String value = "entropy";
        Optional<String> optionalBox = Optional.ofNullable(value);
//        System.out.println(optionalBox.get()); // not recommend
        System.out.println(optionalBox.isPresent());
        System.out.println(optionalBox.isEmpty());

    }
}
