package org.entropy;

@FunctionalInterface
public interface Message {
    String send(String name, String title);
}
