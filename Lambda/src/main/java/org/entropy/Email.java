package org.entropy;

public class Email implements Message {
    String email;

    public Email() {
    }

    @Override
    public void send(String name, String title) {
        System.out.println("This is an email.");
    }
}
