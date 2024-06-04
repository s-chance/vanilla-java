package org.entropy;

public class UserRepository {
    public User findUserByName(String name) {
        if ("entropy".equals(name)) {
            return new User("entropy", "entropy tree");
        } else {
            return null;
        }
    }
}
