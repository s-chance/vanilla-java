package org.entropy;

import java.util.Optional;

public class User {
    String name;
    String fullName;

    public User(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Optional<String> getFullName2() {
        return Optional.ofNullable(fullName);
    }
}
