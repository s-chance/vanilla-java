package org.entropy;

public class GenericPrinter<T, K> {
    T content;

    K content2;

    GenericPrinter(T content, K content2) {
        this.content = content;
        this.content2 = content2;
    }

    public void print() {
        System.out.println(content);
        System.out.println(content2);
    }
}
