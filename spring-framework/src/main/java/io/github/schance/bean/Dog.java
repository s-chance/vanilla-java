package io.github.schance.bean;

import io.github.schance.Component;

@Component(name = "spark")
public class Dog {

    @Autowired
    Cat cat;
}
