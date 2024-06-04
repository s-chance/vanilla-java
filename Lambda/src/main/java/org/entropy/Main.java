package org.entropy;

public class Main {
    public static void main(String[] args) {
        Message email = new Email();
        sendMessage(email);
        Sms sms = new Sms();
        sendMessage(sms);
    }

    // Interface-oriented programming
    static void sendMessage(Message message) {
        message.send();
    }
}
