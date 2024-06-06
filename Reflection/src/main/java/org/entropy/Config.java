package org.entropy;

public class Config {

    @Bean
    public Customer customer() {
        return new Customer("entropy", "entropy@example.com");
    }

    @Bean
    public Address address() {
        return new Address("345 Spear Street", "94105");
    }

    @Bean
    public Message message() {
        return new Message("hello world");
    }
}
