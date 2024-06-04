package org.entropy;

public class Main {
    public static void main(String[] args) {
//        public void send() {
//            System.out.println("This is an email.");
//        }

        // lambda expression can only use for the method which only has one abstract method
        // which called "Functional Interface"
        Message lambda = (name, title) -> {
            System.out.println("This is an email to " + name + " about " + title);
            System.out.println("Here there");
            return "success";
        };
        sendMessage(lambda);
    }

    // Interface-oriented programming
    static void sendMessage(Message message) {
        String status = message.send("entropy", "hello world");
        System.out.println(status);
    }
}
