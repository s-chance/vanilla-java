package io.github.schance;

public interface MyHandler {
    String functionBody(String methodName);

    default void setProxy(MyInterface proxy) {

    }
}
