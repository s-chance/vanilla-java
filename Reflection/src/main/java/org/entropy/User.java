package org.entropy;

import java.util.List;

public class User {

    public String name;
    private final int age;
    private String email;
    private Message message;
    private List<String> comments;
    public static int publicStaticField = 1;
    private static int privateStaticField = 10;

    static {
        System.out.println("UserClass is initialized");
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() {
        this.age = 18;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void myPublicMethod() {
        System.out.println("This is a public method.");
    }

    private void myPrivateMethod() {
        System.out.println("This is a private method.");
    }

    private void myPrivateMethod(String content, String mark) {
        System.out.println("This is a private method with parameters. " + content + mark);
    }

    public static void myPublicStaticMethod() {
        System.out.println("This is a public static method.");
    }

    private static void myPrivateStaticMethod() {
        System.out.println("This is a private static method.");
    }

    private static void myPrivateStaticMethod(String content) {
        System.out.println("This is a private static method with parameters. " + content);
    }
}
