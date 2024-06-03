package org.entropy;

public class Car extends Vehicle implements Thing {
    Integer price;

    String name;

    public Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }

    public Car() {

    }
}
