package io.github.schance;

public class NameLengthImpl implements MyInterface{
    @Override
    public void fn1() {
        String methodName = "fn1";
        System.out.println(methodName);
        System.out.println(methodName.length());
        System.out.println("fn1");
    }

    @Override
    public void fn2() {
        String methodName = "fn2";
        System.out.println(methodName);
        System.out.println(methodName.length());
        System.out.println("fn2");
    }

    @Override
    public void fn3() {
        String methodName = "fn3";
        System.out.println(methodName);
        System.out.println(methodName.length());
        System.out.println("fn3");
    }
}
