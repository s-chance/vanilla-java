# Optional 总结

Optional 普遍用于方法的返回类型，表示方法可能不返回结果

```java
public class UserRepository {
  
  public User findUserByName(String name) {
    if (name.equals("entropy")) {
      User user = new User("entropy", "entropy tree");
      return user;
    } else {
      return null;
    }
  }
}
```

当一个方法可能返回一个值或者什么都不返回，即返回 null 时，可以使用 Optional 包装

```java
public class UserRepository {
  
  public Optional<User> findUserByName(String name) {
    if (name.equals("entropy")) {
      User user = new User("entropy", "entropy tree");
      return Optional.of(user);
    } else {
      return Optional.empty();
    }
  }
}
```



在设计 API 时，它能引导 API 的使用者，告诉他们这个结果可能不存在，并强制调用者处理这种可能性

```java
UserRepository userRepository = new UserRepository();
Optional<User> optionalUser = userRepository.findUserByName("entropy");
```

```java
User user = optionUser.orElseGet(
  () -> new User("default", "default user")
);
```

```java
optionalUser.ifPresentOrElse(
  user -> System.out.println(user.getFullName()),
  () -> System.out.println("User not found")
);
```



尽管很有用，但也有一些不推荐的使用场景

1.不应该用于类的字段

```java
public class User {
  Optional<String> name;
}
```

由于 Optional 对象的创建和管理有一定的开销，它并不适合用作类的字段，使用 Optional 作为字段类型会增加内存消耗，并且会使得对象的序列化变得复杂



2.不应该用于方法的参数

```java
public class User {
  public void updateUser(Optional<String> name) {
    // ...
  }
}
```

将 Optional 用作方法参数会使方法的使用和理解变得复杂，如果希望方法接受一个可能为空的值，通常有更好的设计选择，比如方法重载

```java
public class User {
  public void updateUser(String name) {
    // ...
  }
  public void updateUser() {
    // ...
  }
}
```



3.不应该用于构造函数参数

```java
public class User {
  public User(Optional<String> name) {
    // ...
  }
}
```

类似于方法参数，Optional 也不应用于构造器参数，这种做法还会迫使调用者创建 Optional 实例，应该通过构造器重载来解决

```java
public class User {
  public User(String name) {
    // ...
  }
  public User() {
    // ...
  }
}
```



4.不应该用于集合的参数类型

```java
public Optional<List<User>> getUsers() {
  return Optional.ofNullable(getList());
}
```

如果方法返回一个集合，这个集合可能为空，不应该用 Optional 来包装它。

集合本身已经可以很好地处理空集合的情况，没必要使用 Optional 包装集合

```java
public List<User> getUsers() {
  List<User> users = getList();
  if (users == null) {
    return Collections.emptyList();
  } else {
    return users;
  }
}
```



5.不建议使用 `get()` 方法

```java
String value = optionalValue.get();
```

调用 Optional 的 get 方法前没有确认值是否存在，可能会导致 `NoSuchElementException`

即使是使用 `isPresent` 和 `get` 的组合，也不是最好的选择。这样做其实和直接调用可能为 null 的引用没有区别，仍然需要进行显式的检查，以避免异常。

```java
if (optionalValue.isPresent()) {
  String value = optionalValue.get();
}
```

应当使用 `ifPresent`、`ifPresentOrElse`、`orElse` 或者 `orElseThrow` 等方法

```java
optionalValue.ifPresent(System.out::println);
optionalValue.ifPresentOrElse(
  System.out::println,
  () -> System.out.println("empty");
);
```

```java
String value = optionalValue.orElse("defalut");
String value = optionalValue.orElseGet(() -> "default");
String value = optionalValue.orElseThrow();
```



