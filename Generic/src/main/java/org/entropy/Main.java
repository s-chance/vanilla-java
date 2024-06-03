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
    }
}
