package org.entropy;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        User user = userRepository.findUserByName("entropy2");
        // if you need to process a large number of null types, that will be a tedious work
        if (user != null) {
            System.out.println(user.getFullName());
        } else {
            User defaultUser = new User("default", "default user");
            System.out.println(defaultUser.getFullName());
        }

        String value = "entropy";
        Optional<String> optionalBox = Optional.ofNullable(value);
//        System.out.println(optionalBox.get()); // not recommend
        System.out.println(optionalBox.isPresent());
        System.out.println(optionalBox.isEmpty());

    }
}
