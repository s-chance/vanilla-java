package io.github.schance.bean;

import io.github.schance.Component;

@Component(name = "spark")
public class Dog {

    @Autowired
    Cat cat;

    @Autowired
    Dog dog;

    @PostConstruct
    public void init() {
        System.out.println("dog create " + cat);
        System.out.println("dog create " + dog);
    }
}
