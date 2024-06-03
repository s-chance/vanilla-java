package org.entropy;

public class Main {
    public static void main(String[] args) {
//        GenericPrinter<Car> printer = new GenericPrinter<>(new Car());
        GenericPrinter<Bus> printer = new GenericPrinter<>(new Bus());
        printer.print();
    }
}
