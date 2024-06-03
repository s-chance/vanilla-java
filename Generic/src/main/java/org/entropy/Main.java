package org.entropy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericPrinter<Car> printer = new GenericPrinter<>(new Car());
//        GenericPrinter<Bus> printer = new GenericPrinter<>(new Bus());
        printer.print();

        // type-unsafe problem
        // don't use 'Object' as a Generic Type
        List<Object> list = new ArrayList<>();
        list.add("123");
        list.add(456);

        // this code will throw ClassCastException exception when running
        // String item = (String) list.get(1);

        System.out.println(list);

//        print("hello world");
//        print(123);
//        print(12L);
        print(new Car());
        print2("hello world", 123);

        List<Integer> integers = new ArrayList<>();
        integers.add(123);
        integers.add(456);
        printList(integers);

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Car());
        printVehicleList(vehicles);

        vehicles.add(new Bus());
        printCarList(vehicles);
    }

    private static <T extends Vehicle & Thing> void print(T content) {
        System.out.println(content);
    }

    private static <T, K> void print2(T content, K content2) {
        System.out.println(content);
        System.out.println(content2);
    }

    // List<Integer> is not a subclass of List<Object>
//    private static void printList(List<Object> content) {
//        System.out.println(content);
//    }

    // using wildcard to match all types
    // wildcard will lose the specific type
    // if you don't need to make a modification and want to streamline code, you can use wildcard
    private static void printList(List<?> content) {
        System.out.println(content);
    }

    // upper bound wildcard
    private static void printVehicleList(List<? extends Vehicle> content) {
        System.out.println(content);
    }

    // lower bound wildcard
    private static void printCarList(List<? super Car> content) {
        System.out.println(content);
    }
}
