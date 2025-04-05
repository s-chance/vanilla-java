package io.github.schance;

import lombok.Data;

@Data
@Table(tableName= "user")
public class User {

    private Integer id;
    private String name;
    private Integer age;

}
