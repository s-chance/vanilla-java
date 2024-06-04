package org.entropy;

public class Sms implements Message {
    String phoneNumber;

    public Sms() {
    }

    @Override
    public void send(String name) {
        System.out.println("This is a sms.");
    }
}
