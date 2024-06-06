# Reflection 笔记

Java 中的反射 Reflection 是一个强大的机制，它使得程序能够在运行时反观和修改其内部结构。

具体来说，反射允许程序在运行时动态地操作类和对象，包括创建对象、访问字段和调用方法等等。

对于构建灵活的应用实现复杂的框架至关重要。



现在有一个 org.entropy.User 类

```java
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
```

org.entropy.Main

```java
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
```

这种直接访问的方式清晰直观，但在某些场景中，需要在运行时动态地操作这些成员。比如在运行时根据数据库中提供的类名或方法名，或者基于字符串变量来动态实例化对象或调用方法时，这种方式就不再适用了。

反射机制恰好提供了及解决这类需求的能力，其关键在于一个特殊的对象，称之为类对象。



Class 对象是由 Java 虚拟机 JVM 在加载类时自动创建的，用于存储类的信息。通过这个 Class 对象，就能访问类的结构以及对类本身和它的实例进行操作。

虚拟机创建 Class 对象的过程是这样的，当我们编写一个类并完成编译之后，编译器会将其转化为字节码，存储在 .class 后缀的文件中。接下来在类的加载过程中，虚拟机利用 Class Loader 读取这个 .class 文件，将其中的字节码加载到内存中，并基于这些信息创建相应的 Class 对象。由于每个类在 JVM 中只加载一次，所以每个类都对应着一个唯一的 Class 对象。



创建 User 类的 Class 对象

获取 Class 对象有三种主要方式，第一种是使用类字面常量，`类的名称.class`

```java
Class<User> userClass = User.class;
```

这也是获取 Class 对象最直接的方式，它在编译时就确定了具体的类，属于静态引用。使用这种方式获得类的 Class 对象时不会立即触发类的初始化。也就是说这段代码并不会触发 User 类静态代码块的执行，只有在访问类的静态成员或者创建该类实例的时候才会被触发。



第二种就是使用对象的 getClass 方法，如果已经有了某个类的实例对象，可以调用该对象的 getClass 方法来获取它的 Class 对象

```java
User user = new User("entropy", 22);
Class<? extends User> clazz = user.getClass();
```

这边的泛型参数用通配符，是因为这个 Class 对象是在运行时从 User 实例获取的，而 User 实例的具体类型只能在运行时创建和确定，无法准确地判断 Class 对象的确切类型。因此使用通配符来表示未知的类型，直接指定 User 会报错。



第三种就是使用 Class 的 forName 静态方法，这种方法用于在运行时动态加载指定的类，并返回该类的 Class 对象实例。通常用于类名在编译时不可知的场景中。

`Class.forName()` 它接受一个字符串参数，表示待加载类的完全限定名 fully qulified name，它包括包名和类名

```java
Class<?> clazz = Class.forName("org.entropy.User");	
```

它也是运行时才能确定类型而不是编译阶段，所以这边也用通配符。

通过这种方法获得类的 Class 对象时，会立即触发类的初始化。类的静态初始化块 static 块会被执行。



Class 对象的核心方法

`getDeclaredFields` 获得当前类的所有字段，无论访问权限如何，public 或 private 都可以，但是无法获得其父类的字段。返回一个 Field 类型的数组。

```java
Field[] fields = clazz.getDeclaredFields();
for (Field field : fields) {
  System.out.println(field.getName());
}
```

如果只想获得 public 字段，可以使用 `getFields` 方法。

```java
Field[] fields = clazz.getFields();
for (Field field : fields) {
  System.out.println(field.getName());
}
```

这个方法只能获得类的所有 public 字段，如果有父类的话，也包括其父类的 public 字段。

org.entropy.Person

```java
public class Person {
    public String personPublicField;
    private String personPrivateField;
}
```

org.entropy.User

```java
public class User extends Person {}
```

这里可以使用 `getFields` 方法获得 `Person` 类的 `personPublicField` 字段，`getDeclaredFields` 方法不会获得任何父类的字段，只会获得当前类字段。

如果想获得父类的所有字段，可以先通过 `getSuperClass` 方法来获得父类的 Class 对象，然后再获得它的所有字段

```java
Field[] fields = clazz.getSuperclass().getDeclaredFields();
```

同样的，在获取类的其他成员信息，比方说方法和注解时，使用的方法也是类似的。



总结来说，`getDeclared` 系列的方法用于访问一个类中声明的所有成员，不管它们的访问权限如何，而不带 Declared 的方法仅用于获取公共成员，包括从父类继承的公共成员。

另外也可以通过指定字段名来获取某一个字段。

```java
Field field = clazz.getDeclaredField("name");
System.out.println(field.getType());
System.out.println(field.getDeclaredAnnotation(MyAnnotation.class));
```

org.entropy.MyAnnotation

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyAnnotation {
}
```

如果字段类型是泛型类型，返回是类型擦除后的原始类型。需要获得泛型类型的完整类型信息，就要使用 `getGenericType` 方法。

如果字段名字不存在，编译器不会报错，但会在执行时抛出 NoSuchFieldException。这是因为通过反射获取字段的过程是在运行时动态执行的，无法在编译阶段进行错误的检测和捕获。错误只能在程序执行时被发现和处理，因此在使用反射时需要格外谨慎，并确保实施充分的错误处理机制



首先关注类层面的操作，包括查看和修改静态字段值以及调用静态方法。

获取静态字段值，使用 `get` 方法

```java
Class<?> clazz = Class.forName("org.entropy.User");
Field field = clazz.getDeclaredField("publicStaticField");
System.out.println(field.get(null));
```

访问私有字段会出现权限不足的错误，这个错误在编译阶段不会被捕获，只有在程序实际运行时才会被捕捉，为了能够访问私有字段，可以使用 `setAccessible` 方法来设置访问权限

```java
Field field = clazz.getDeclaredField("privateStaticField");
field.setAccessible(true);
field.set(null, 100);
System.out.println(field.get(null));
```

获取所有的方法

```java
Method[] methods = clazz.getDeclaredMethods();
for (Method method : methods) {
  System.out.println(method.getName());
}
```

调用静态方法

```java
Method method = clazz.getDeclaredMethod("myPublicStaticMethod");
method.invoke(null);
```

调用私有静态方法

```java
Method method = clazz.getDeclaredMethod("myPrivateStaticMethod");
method.setAccessible(true);
method.invoke(null);
```

调用带参数的私有静态方法

```java
Method method = clazz.getDeclaredMethod("myPrivateStaticMethod", String.class);
method.setAccessible(true);
method.invoke(null, "hello world");
```



接下来使用反射来创建类的实例，并对实例的字段值和方法进行访问和操作。

在反射中，我们通常通过获取类的构造器来创建实例。简单来说，就是先从目标类的 Class 对象中选择一个合适的构造器，然后通过调用它的 `newInstance` 方法来创建对象

无参构造器

```java
Constructor<?> constructor = clazz.getDeclaredConstructor();
```

有参构造器

```java
Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
```

实例化对象

```java
Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
Object obj = constructor.newInstance("entropy", 55);
if (obj instanceof User) {
  User user = (User) obj;
}
```

也可以使用 Class 对象的自带方法 `cast` 进行转换。不过由于 `cast` 方法是使用泛型类型转换的，这就要求必须在编译阶段确定要转换带的类型。泛型仅在编译期间生效，因此这个方法仅对通过 `类字面常量.class` 获取的 Class 对象有效，而对于通过其他动态方式获得的 Class 对象则无效。



类型转换的写法并不是使用反射的最佳实践，建议使用反射直接调用和操作对象的方法和字段，而不是先进行类型转换，如果在编写阶段已经明确了要转换的类型，那么直接显式地调用更合适而不必依赖于反射。反射的真正价值在于处理编译时未知的类型，从而编写更具有通用性的代码。



接下来操作一下实例对象里的字段值和方法

所有的方法其实和操作类的静态字段值、调用静态方法是一样的，唯一的区别就是把之前参数 null 变成对应的实例对象。

访问 name 字段

```java
Class<?> clazz = Class.forName("org.entropy.User");
Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
Object obj = constructor.newInstance("entropy", 55);
Field field = clazz.getDeclaredField("name");
System.out.println(field.get(obj));
```

访问 age 字段

```java
Class<?> clazz = Class.forName("org.entropy.User");
Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
Object obj = constructor.newInstance("entropy", 55);
Field field = clazz.getDeclaredField("age");
field.setAccessible(true);
System.out.println(field.get(obj));
```

修改 age 的值

```java
field.set(obj, 33);
```

通过反射即便是私有字段值也是可以随意访问和修改的，包括 final 字段。而 final 字段通常被视为不可变，这其实破环了 Java 的封装性和设计原则，可能导致不可预见的副作用，尽量避免使用。



接下来调用实例的方法

公有方法

```java
Object obj = constructor.newInstance("entropy", 55);
Method method = clazz.getDeclaredMethod("myPublicMethod");
method.invoke(obj);
```

私有方法

```java
Object obj = constructor.newInstance("entropy", 55);
Method method = clazz.getDeclaredMethod("myPrivateMethod");
method.setAccessible(true);
method.invoke(obj);
```

带参数的私有方法

```java
Object obj = constructor.newInstance("entropy", 55);
Method method = clazz.getDeclaredMethod("myPrivateMethod", String.class, String.class);
method.setAccessible(true);
method.invoke(obj, "hello world", "!");
```



为了更好地理解反射的实际应用场景，简单模拟一下框架功能创建一些通用方法，使得可以通过配置实现服务的依赖注入。

假设有这样一个需求：我们需要通过一个订单 Order 对象来获取用户信息和地址信息

Order 类的结构

```java
public class Order {
  private Customer customer;

  private Address address;

  public Order(Customer customer, Address address) {
    this.customer = customer;
    this.address = address;
  }

  public Order() {
  }

  public Customer getCustomer() {
    return customer;
  }

  public Address getAddress() {
    return address;
  }
}
```

org.entropy.Main

```java
public class Main {
  public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
    Address address = new Address("345 Spear Street", "94105");
    Customer customer = new Customer("entropy", "entropy@example.com");
    Order order = new Order(customer, address);
    order.getCustomer().printName();
    order.getAddress().printStreet();
  }
}
```

org.entropy.Address

```java
public class Address {
  private String street;
  private String postCode;

  public Address(String street, String postCode) {
    this.street = street;
    this.postCode = postCode;
  }

  public String getStreet() {
    return street;
  }

  public String getPostCode() {
    return postCode;
  }

  public void printStreet() {
    System.out.println("Address street: " + street);
  }

  public void printPostCode() {
    System.out.println("Address postcode: " + postCode);
  }
}
```

org.entropy.Customer

```java
public class Customer {
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void printName() {
        System.out.println("Customer name: " + name);
    }

    public void printEmail() {
        System.out.println("Customer email: " + email);
    }
}
```

这种方法在面对动态场景时存在限制，一旦代码完成，就无法动态地创建对象或调用方法。



接下来使用反射的方式来实现

首先定义一个 Config 类，在其中定义 Customer 和 Address 的服务，这些服务可以通过定义相应的方法来获取，这些方法负责实例化并返回我们需要的对象

org.entropy.Config

```java
public class Config {

  public Customer customer() {
    return new Customer("entropy", "entropy@example.com");
  }

  public Address address() {
    return new Address("345 Spear Street", "94105");
  }

  public Message message() {
    return new Message("hello world");
  }
}
```

org.entropy.Message

```java
public class Message {
  private String content;

  public Message(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void printMessage() {
    System.out.println("Message: " + this.content);
  }
}
```

再定义一个 Container 类来注册服务和获得实例

org.entropy.Container

```java
public class Container {
  private Map<Class<?>, Method> methods;
}
```

键的类型为 Class 表示方法返回的类型，值的类型为 Method 表示对应的方法。这里存储的是方法本身，而不是方法执行后获得的实例。具体的实例将在使用时执行这些方法来获取，这种做法可以节省资源并提高性能。

再创建一个 config 字段用来存放 Config 的实例，之后用来调用方法获得服务实例

```java
public class Container {
  private Map<Class<?>, Method> methods;
  private Object config;
}
```

添加 init 方法

```java
public void init() throws ClassNotFoundException {
  Class<?> clazz = Class.forName("org.entropy.Config");
  Method[] methods = clazz.getDeclaredMethods();
  for (Method method : methods) {
    System.out.println(method.getName());
  }
}
```

org.entropy.Main

```java
Container container = new Container();
container.init();
```

测试一下，打印了所有的方法，但是现在只想要 address 和 customer 服务，而不需要其他方法。这个时候可以通过注释的方式来声明或者筛选具体的方法。

org.entropy.Bean

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
}
```

在 Config 类中需要的方法上添加自定义注解

```java
public class Config {

  @Bean
  public Customer customer() {
    return new Customer("entropy", "entropy@example.com");
  }

  @Bean
  public Address address() {
    return new Address("345 Spear Street", "94105");
  }

  public Message message() {
    return new Message("hello world");
  }
}
```

在 Container 类的 init 方法中筛选需要的方法

```java
public void init() throws ClassNotFoundException {
  Class<?> clazz = Class.forName("org.entropy.Config");
  Method[] methods = clazz.getDeclaredMethods();
  for (Method method : methods) {
    if (method.getDeclaredAnnotation(Bean.class) != null) {
      System.out.println(method.getName());
    }
  }
}
```

获得到需要的方法名后，将方法名存储到 methods 中，并获得 config 实例

```java
public void init() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
  this.methods = new HashMap<>();
  Class<?> clazz = Class.forName("org.entropy.Config");
  Method[] methods = clazz.getDeclaredMethods();
  for (Method method : methods) {
    if (method.getDeclaredAnnotation(Bean.class) != null) {
      this.methods.put(method.getReturnType(), method);
    }
  }
  this.config = clazz.getConstructor().newInstance();
}
```

编写一个方法，通过类的 Class 对象获取相应的服务实例，命名为 getServiceInstanceByClass。使用 Class 对象作为键从事先初始化并加载好的服务方法的 Map 集合中查找对应的方法，如果找到了相应的方法就执行该方法以实例化服务对象并返回这个对象。

```java
public Object getServiceInstanceByClass(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
  if (this.methods.containsKey(clazz)) {
    Method method = this.methods.get(clazz);
    Object obj = method.invoke(this.config);
    return obj;
  }
  return null;
}
```

在 Main 中调用方法

```java
public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
  Container container = new Container();
  container.init();
  Class<?> clazz = Class.forName("org.entropy.Customer");
  Object obj = container.getServiceInstanceByClass(clazz);
  System.out.println(obj);
}
```



这里的 `getServiceInstanceByClass` 方法存在一个问题，每个调用这个方法以获取服务实例时，都会重新创建一个新对象，这在性能上可能引起一个问题。因为许多服务都是全局的，需要在很多地方使用，我们不希望每次都创建新的服务对象。理想的做法是创建一次之后就复用这个实例。

为了实现这一点，可以这样优化代码。

在 Container 类中补充字段 services，存储已经创建的实例

```java
private Map<Class<?>, Object> services;
```

在 `init` 方法中初始化 services

```java
public void init() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
  this.methods = new HashMap<>();
  this.services = new HashMap<>();
  Class<?> clazz = Class.forName("org.entropy.Config");
  Method[] methods = clazz.getDeclaredMethods();
  for (Method method : methods) {
    if (method.getDeclaredAnnotation(Bean.class) != null) {
      this.methods.put(method.getReturnType(), method);
    }
  }
  this.config = clazz.getConstructor().newInstance();
}
```

在 `getServiceInstanceByClass` 方法中存储实例以复用

```java
public Object getServiceInstanceByClass(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        if (this.services.containsKey(clazz)) {
            return this.services.get(clazz);
        } else {
            if (this.methods.containsKey(clazz)) {
                Method method = this.methods.get(clazz);
                Object obj = method.invoke(this.config);
                this.services.put(clazz, obj);
                return obj;
            }
        }
        return null;
    }
```



接下来定义一个 `createInstance` 方法，用于通过 Class 对象创建普通实例，并且实现将服务自动注入到对象里面。

```java
public Object createInstance(Class<?> clazz) {
  Constructor<?>[] constructors = clazz.getDeclaredConstructors();
  for (Constructor<?> constructor : constructors) {
    System.out.println(constructor);
  }
  return null;
}
```

在 Main 方法中测试

```java
Class<?> clazz = Class.forName("org.entropy.Order");
container.createInstance(clazz);
```

测试一下，有两个构造器，依然可以用自定义注解的方式指定使用哪一个构造器。

org.entropy.Autowired

```java
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
```

在 Customer 类中指定的构造器上添加自定义注解

```java
@Autowired
public Order(Customer customer, Address address) {
  this.customer = customer;
  this.address = address;
}
```

在 `createInstance` 方法中筛选

```java
for (Constructor<?> constructor : constructors) {
  if (constructor.getDeclaredAnnotation(Autowired.class) != null) {
    System.out.println(constructor);
  }
}
```

接下来通过这个带参数的构造器来实例化对象

```java
public Object createInstance(Class<?> clazz) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
  Constructor<?>[] constructors = clazz.getDeclaredConstructors();
  for (Constructor<?> constructor : constructors) {
    if (constructor.getDeclaredAnnotation(Autowired.class) != null) {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      Object[] arguments = new Object[parameterTypes.length];
      for (int i = 0; i < parameterTypes.length; i++) {
        arguments[i] = getServiceInstanceByClass(parameterTypes[i]);
      }
      return constructor.newInstance(arguments);
    }
  }
  return clazz.getConstructor().newInstance();
}
```

在 Main 方法中获取实例对象

```java
Container container = new Container();
container.init();
Class<?> clazz = Class.forName("org.entropy.Order");
Object obj = container.createInstance(clazz);
Field field = clazz.getDeclaredField("customer");
field.setAccessible(true);
Object fieldValue = field.get(obj);
System.out.println(fieldValue);
```

打印对象中的方法

```java
Container container = new Container();
container.init();
Class<?> clazz = Class.forName("org.entropy.Order");
Object obj = container.createInstance(clazz);
Field field = clazz.getDeclaredField("customer");
field.setAccessible(true);
Object fieldValue = field.get(obj);
Method[] methods = fieldValue.getClass().getDeclaredMethods();
for (Method method : methods) {
  System.out.println(method.getName());
}
```

需要获得指定方法同样可以通过自定义注解获得

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Printable {
}
```

Customer 中的 `printName` 方法

```java
@Printable
public void printName() {
  System.out.println("Customer name: " + name);
}
```

```java
for (Method method : methods) {
  if (method.getDeclaredAnnotation(Printable.class) != null) {
    method.invoke(fieldValue);
  }
}
```

如果想执行 Customer 中的多个方法，就在对应的方法上都添加自定义注解。

也可以把这些类名和字段名单独放到变量里面，方便配置

```java
Container container = new Container();
container.init();
String className = "org.entropy.Order";
String fieldName = "address";
Class<?> clazz = Class.forName(className);
Object obj = container.createInstance(clazz);
Field field = clazz.getDeclaredField(fieldName);
field.setAccessible(true);
Object fieldValue = field.get(obj);
Method[] methods = fieldValue.getClass().getDeclaredMethods();
for (Method method : methods) {
  if (method.getDeclaredAnnotation(Printable.class) != null) {
    method.invoke(fieldValue);
  }
}
```

这个时候类的初始化以及方法的调用只需要通过修改对应的变量和添加自定义注解即可实现。

下面演示将 User 类和 Message 服务进行注册

User 类中添加 @Autowired 注解，用于识别 User 类初始化并通过 Container 返回实例化对象

```java
@Autowired
public User(Message message) {
  this.message = message;
  this.age = 18;
}
```

Message 类中添加 @Printable 注解，用于识别 printMessage 方法并执行

```java
@Printable
public void printMessage() {
  System.out.println("Message: " + this.content);
}
```

Config 类中添加 @Bean 注解，用于识别 Messaga 服务并注册到 Container 中

```java
@Bean
public Message message() {
  return new Message("hello world");
}
```



采用这种设计，我们可以看到对象的实例化和方法的调用不再依赖于硬编码的类名和方法名，而是基于字符串和注解所提供的配置信息进行，这使得代码非常的灵活且通用，特别适用于编写需要高度解耦的框架和应用程序。

当然在实际框架中所采用的方法要复杂和安全的多，涉及不同的工作流程、策略及优化措施。

虽然反射为程序设计带来了极大的灵活性和功能性，但它也带来了一些挑战，包括性能开销、安全性问题和维护的复杂性。因此设计者在使用反射时必须谨慎，以便在灵活性和这些潜在问题之间找到平衡点。

在常规的应用程序级别编程中通常推荐直接使用具体的类和方法，这样有助于保持代码的可读性和提高运行性能。
