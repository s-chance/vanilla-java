package org.entropy;

public class Main {
    public static void main(String[] args) {
        User user = new User("entropy", 22);
        Class<? extends User> clazz = user.getClass();
    }
}
