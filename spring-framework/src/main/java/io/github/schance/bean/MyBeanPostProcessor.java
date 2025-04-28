package io.github.schance.bean;

import io.github.schance.BeanPostProcessor;
import io.github.schance.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println(beanName + " initialized");
        return bean;
    }
}
