package io.github.schance.bean;

import io.github.schance.Component;

@Component
public class Cat {

    @Autowired
    private Dog dog;

    @PostConstruct
    public void init() {
        System.out.println("cat init " + dog);
    }
}
