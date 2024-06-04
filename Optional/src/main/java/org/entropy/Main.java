package org.entropy;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        User user = userRepository.findUserByName("entropy2");
        if (user != null) {
            System.out.println(user.getFullName());
        } else {
            User defaultUser = new User("default", "default user");
            System.out.println(defaultUser.getFullName());
        }
    }
}
