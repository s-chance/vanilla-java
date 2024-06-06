package org.entropy;

public class Message {
    private String content;

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Printable
    public void printMessage() {
        System.out.println("Message: " + this.content);
    }
}
