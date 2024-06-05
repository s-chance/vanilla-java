package org.entropy;

public class Main {
    public static void main(String[] args) {
        int field = User.publicStaticField;
        System.out.println(field);
        User.myPublicStaticMethod();

        User user = new User("entropy", 31);
        System.out.println(user.name);
        user.myPublicMethod();
    }
}
