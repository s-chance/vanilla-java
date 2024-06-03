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
    }

    private static <T extends Vehicle & Thing> void print(T content) {
        System.out.println(content);
    }

    private static <T, K> void print2(T content, K content2) {
        System.out.println(content);
        System.out.println(content2);
    }
}
