package org.entropy;

public class Main {
    public static void main(String[] args) {
//        public void send() {
//            System.out.println("This is an email.");
//        }
        sendMessage((name, title) -> {
            System.out.println("This is an email to " + name + " about " + title);
            System.out.println("Here there");
            return "success";
        });
    }

    // Interface-oriented programming
    static void sendMessage(Message message) {
        String status = message.send("entropy", "hello world");
        System.out.println(status);
    }
}
