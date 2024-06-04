package org.entropy;

import java.util.Optional;

public class UserRepository {
    public Optional<User> findUserByName(String name) {
        if ("entropy".equals(name)) {
            return Optional.of(new User("entropy", "entropy tree"));
        } else {
            return Optional.empty();
        }
    }
}
