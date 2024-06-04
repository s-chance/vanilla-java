package org.entropy;

public class Sms implements Message {
    String phoneNumber;

    public Sms() {
    }

    @Override
    public String send(String name, String title) {
        System.out.println("This is a sms.");
        return "success";
    }
}
