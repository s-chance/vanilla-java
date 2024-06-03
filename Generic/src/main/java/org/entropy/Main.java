package org.entropy;

public class Main {
    public static void main(String[] args) {
        GenericPrinter<String, Integer> printer = new GenericPrinter<>("hello world", 123);
        printer.print();
    }
}
