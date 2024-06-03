package org.entropy;

public class StringPrinter {
    String content;

    StringPrinter(String content) {
        this.content = content;
    }

    public void print() {
        System.out.println(content);
    }
}
