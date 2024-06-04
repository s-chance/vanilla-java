package org.entropy;

public class Main {
    public static void main(String[] args) {
//        public void send() {
//            System.out.println("This is an email.");
//        }
        sendMessage((name) -> {
            System.out.println("This is an email to " + name);
            System.out.println("Here there");
        });
    }

    // Interface-oriented programming
    static void sendMessage(Message message) {
        message.send("entropy");
    }
}
