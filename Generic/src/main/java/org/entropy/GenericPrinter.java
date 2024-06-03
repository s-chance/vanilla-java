package org.entropy;

public class GenericPrinter<T extends Vehicle> {
    T content;


    GenericPrinter(T content) {
        this.content = content;
    }

    public void print() {
        System.out.println(content);;
    }
}
