#  Stream API 笔记

Stream API 的出现，显著推进了 Java 对函数式编程的支持，它允许开发者可以用声明式 Declarative 的方式处理数据集合，比如列表、数组等。还能有效利用多核处理器进行并行操作，提升应用程序的性能，同时保持代码的简洁易读。

假设有以下人员名单，我们的任务是找出所有年龄超过 18 岁的人，并将他们的信息收集到另外一个名单里

```java
public class Main {
  public static void main(String[] args) {
    List<Person> people = List.of(
      new Person("Peter", 33, "USA"),
      new Person("Brain", 10, "USA"),
      new Person("Jack", 12, "UK"),
      new Person("Alex", 22, "USA"),
      new Person("Steven", 24, "FR")
    );
  }
}
```

org.entropy.Person

```java
public class Person {
  private String name;
  private int age;
  private String country;

  public Person(String name, int age, String country) {
    this.name = name;
    this.age = age;
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getCountry() {
    return country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person person)) return false;

    if (age != person.age) return false;
    if (!Objects.equals(name, person.name)) return false;
    return Objects.equals(country, person.country);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + age;
    result = 31 * result + (country != null ? country.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Person{" +
      "name='" + name + '\'' +
      ", age=" + age +
      ", country='" + country + '\'' +
      '}';
  }
}
```

在没有 Stream API 之前，通常会用命令式编程 Imperative programming 的方式编写一个循环来逐一检查名单上每个人的年龄，然后将年龄超过 18 岁的人添加到新的集合中。

```java
List<Person> adults = new ArrayList<>();
for (Person person : people) {
  if (person.getAge() > 18) {
    adults.add(person);
  }
}
System.out.println(adults);
```

这种方式岁虽然直观，但是当操作变得复杂时，代码会变得冗长且难以维护。

用 Stream API 实现

```java
List<Person> adults = people.stream()
  .filter(person -> person.getAge() > 18)
  .collect(Collectors.toList());
System.out.println(adults);
```

Stream API 的引入为数据集合的操作提供了一种新的范式。Stream 本身并不是数据结构，不会存储或改变数据源，它仅定义处理方式。可以视为一种高级迭代器，不仅能够支持顺序处理，还能进行并行处理，为集合的过滤、映射、聚合等操作提供了一种高效的实现方式。



要掌握和有效使用 Stream API，关键在于理解其三个核心步骤

第一步：创建流 Stream Creation

流可以从多种数据源创建，包括集合、数组、文件、IO 通道等等，甚至可以创建无限流。由于 Stream 本身并不是数据结构，这意味着当你创建一个 Stream 实例的时候，实际的数据仍然存储在原始的数据结构中。

第二步：中间操作 Intermediate Operations

中间操作用于对流中的元素进行处理，比如筛选、映射、排序等等。每次调用中间操作时，都会返回一个新的流，从而支持链式调用，可以构建复杂的查询和数据处理策略。另外很重要的一点是，中间操作是惰性执行的，意味着他们不会立即执行，直到遇到终端操作才会实际执行。

第三步：终端操作 Terminal Operations

终端操作是整个流处理的实际执行部分，它会触发所有之前定义的中间操作并生成最终结果。比如收集数据到集合中、执行规约、聚合统计以及遍历元素等任务。执行终端操作后，流动的元素会被消费，流就不能再被使用了。



## 创建流

先来看一下如何创建流

流可以通过多种方式生成，每种方法适用于不同的场景，比较常用的就是从集合创建流。对于任何实现了 Collection 接口的集合，比如 List、Set 可以通过 `stream` 方法直接创建一个 Stream 流。

```java
public class Main {
  public static void main(String[] args) {
    List<String> list = List.of("a", "b", "c");
    Stream<String> stream = list.stream();
    stream.forEach(System.out::println);
  }
}
```

其次，我们也可以通过数组创建流，可以使用 `Arrays` 工具类的 `stream` 方法

```java
public class Main {
  public static void main(String[] args) {
    String[] array = {"a", "b", "c"};
    Stream<String> stream = Arrays.stream(array);
    stream.forEach(System.out::println);
  }
}
```

还可以通过 `Stream.of` 的方法，直接从一组值创建一个流

```java
public class Main {
  public static void main(String[] args) {
    Stream<String> stream = Stream.of("a", "b", "c");
    stream.forEach(System.out::println);
  }
}
```

另外想合并两个流的话，可以使用 `concat` 方法，它会按照顺序将两个流的元素连接起来

```java
public class Main {
  public static void main(String[] args) {
    Stream<String> stream1 = Stream.of("a", "b", "c");
    Stream<String> stream2 = Stream.of("d", "e", "f");
    Stream<String> concat = Stream.concat(stream1, stream2);
    concat.forEach(System.out::println);
  }
}
```

以上几个方法创建的流都是从固定元素或数据结构中创建的，元素数量和内容在创建时就已经确定。如果想动态地构建一个流，根据特定条件动态地决定是否将元素加入流中，可以使用 StreamBuilder 流构建器来添加元素和创建流。

```java
public class Main {
  public static void main(String[] args) {
    Stream.Builder<String> streamBuilder = Stream.builder();
    streamBuilder.add("a");
    streamBuilder.add("b");
    if (Math.random() > 0.5) {
      streamBuilder.add("c");
    }
    Stream<String> stream = streamBuilder.build();
    stream.forEach(System.out::println);
  }
}
```

要注意的是，一旦调用了 `build` 方法就不能再向 StreamBuilder 里面添加更多元素，尝试添加会抛出 IllegalStateException 的错误。



从文件创建流也是一个非常实用的功能，特别适合于文本分析，日志文件处理等场景。可以通过 Java 的 Files 类的 lines 方法来实现。

```java
Path path = Paths.get("file.txt");
Stream<String> lines = Files.lines(path);
```

通过 `Files.lines` 打开指定路径的文件，同时它会逐行读取文件的内容，每行文本都会被当作一行字符串处理，然后将其作为元素放入流中。因此如果文件有 n 行，返回的流就会有 n 行，每个元素是一行文本。需要注意的是，使用该方法打开的文件资源必须被妥善关闭，以避免资源泄露，为此推荐将此操作封装在 try with resource 语句里面，这样可以自动确保无论处理过程如何，文件资源都得以关闭。

```java
Path path = Paths.get("file.txt");
try (Stream<String> lines = Files.lines(path)) {
  lines.forEach(System.out::println);
} catch (IOException e) {
  e.getStackTrace();
}
```

此外对于基本类型的处理，Stream API 专门提供了 IntStream、LongStream 和 DoubleStream 来分别处理 int、long 和 double 类型，通过使用 range 和 rangeClosed 等方法可以方便地创建这些基本类型的流。

以 IntStream 为例，先创建一个包含指定元素的 IntStream 流

```java
IntStream intStream = IntStream.of(1, 2, 3);
intStream.forEach(System.out::println);
```

也可以使用 range 方法来创建一个指定范围的数值流，这里保留 1-3 的数字，不包括 4

```java
IntStream intStream = IntStream.range(1, 4);
intStream.forEach(System.out::println);
```

如果想要包含 4 可以用 `rangeClosed` 方法。

可以使用 Random 类生成一个包含随机整数的流

```java
new Random().ints(5)
  .forEach(System.out::println);
```

把基本类型的流转换为对象流，可以通过在基本类型上调用 boxed

```java
IntStream intStream = IntStream.rangeClosed(1, 4);
Stream<Integer> integerStream = intStream.boxed();
```



最后来看一下如何创建无限流。

无限流顾名思义，就是没有固定大小的流，它可以无限地生成元素，可以通过 generate 和 iterate 这两个方法来创建。处理无限流时需要谨慎，防止无限循环的发生，因此通常会结合 limit 等操作来限制流的元素数量。

先来看一下 generate 方法，它主要用于生成重复的值或者生成随机数据。接受一个 Suppiler 类型的参数，可以用 Lambda 表达式来实现一下。创建一个流，里面包含着相同的字符串元素，为防止无限流生成，这里再调用一个中间操作 limit 来限制生成个数

```java
Stream<String> constantStream = Stream.generate(() -> "entropy").limit(5);
constantStream.forEach(System.out::println);
```

也可以利用 Math 类来生成随机数流，比如创建一个流，其中包含 5 个 Double 类型的随机数

```java
Stream.generate(Math::random).limit(5).forEach(System.out::println);
```



再来看一下 iterate，它主要用来生成数学序列或实现迭代算法，我们来生成一个有界的等差数列

```java
Stream<Integer> iterateStream = Stream.iterate(0, n -> n + 2).limit(10);
iterateStream.forEach(System.out::println);
```

还可以用增强版的 iterate 方法，支持定义终止条件来生成有界序列，包含三个参数：起始元素、用来确定终止条件的条件逻辑函数、用来生成下一个元素的函数

```java
Stream<Integer> iterateStream = Stream.iterate(0, n -> n <= 10, n -> n + 2);
iterateStream.forEach(System.out::println);
```



默认情况下创建的流都是顺序流，但我们也可以通过并行流来加速处理流中的元素，从而提升性能。

创建并行流的方法也很直接，对已有的顺序流，我们可以通过调用其 parallel 方法转换为并行流

```java
Stream<Integer> iterateStream = Stream.iterate(0, n -> n <= 10, n -> n + 2);
iterateStream.parallel();
```

而对于集合则可以直接调用 parallelStream 方法来获取并行流

```java
List.of("a", "b", "c").parallelStream().forEach(System.out::println);
```



以上是创建流的主要方式。再来回顾一下，我们可以从集合创建流，从数组创建流，使用 `Stream.of` 方法，使用 `StreamBuilder` 方法，从文件创建流。

基础类型流创建、无限流创建、并行流的创建。



## 中间操作

在成功创建流之后，将进入流的中间操作 Intermediate Operation

中间操作用于对流中的元素进行处理，中间操作可以分为以下几个功能类别：

筛选和切片 (Filtering 和 Slicing)，用于过滤或缩减流中的元素数量

映射 Mapping，这类操作用于转换流中的元素或提取元素的特定属性

排序 Sorting，就是用于对流中的元素进行排序



### 筛选和切片

先来看一下筛选和切片的操作

filter 方法接受一个 Predicate 函数式接口

```java
List<Person> people = List.of(
  new Person("Peter", 33, "USA"),
  new Person("Brain", 10, "USA"),
  new Person("Jack", 12, "UK"),
  new Person("Alex", 22, "USA"),
  new Person("Steven", 24, "FR")
);
people.stream()
  .filter(person -> person.getAge() > 18)
  .forEach(System.out::println);
```

有的时候流中会有重复的数据，如果我们想去除重复的元素，确保流中的每个元素都是唯一的话，可以用 distinct 方法，内部会通过维护一个 HashSet 来追踪哪些元素已经遇到过，从而过滤掉重复的元素

```java
Stream.of("apple", "orange", "apple", "orange", "banana")
  .distinct()
  .forEach(System.out::println);
```

如果流中的元素是自定义对象，就像前面的 Person，你就需要确保这些对象的类正确重写了 equals 和 hashCode 方法。因为 distinct 操作依靠这些方法来确定对象是否相等，以此去除重复的元素。

还可以用 limit 方法截取流中的指定的前几个元素，如果指定的个数大于流的总长度，那就返回整个流。

```java 
people.stream()
  .limit(3)
  .forEach(System.out::println);
```

与 limit 作用相反的操作就是 skip，用于省略流中的指定的前几个元素。当跳过的数量多于流的元素的话，结果就返回一个空的流

```java
people.stream()
  .skip(3)
  .forEach(System.out::println);
```



### 映射

接下来聊一下映射，映射本质上是一个数据转换的过程。假如我们要把包含人员对象的流转换为仅包含人名的流，那该如何处理？

map 方法能轻松完成这一转换，它可以通过提供的函数将流中的每个元素转换成新的元素，最后生成一个新元素构成的流。

map 方法接受一个类型为 Function 的函数式接口作为参数，其中 T 是原始流中的元素类型，而 R 是经过转换后返回的元素类型

```java
people.stream().map(Person::getName).forEach(System.out::println);
```

map 方法适用于单层结构的流进行元素一对一的转换，例如更改数据类型或提取信息，但对于嵌套的集合，数组或其他多层结构的数据处理不够灵活。在这些情况下，flatMap 成为更合适的选择，它不仅能够执行 map 的转换功能，还能够扁平化多层数据结构，将它们转换合并成一个单层流。

过程是这样的，首先它先创建一个流，以下一层结构的数据作为流的元素，然后再将该数据结构元素转换为独立的流，最后将它们合并成一个单层流。

假设有以下嵌套的 list

```java
List<List<Person>> peopleGroups = List.of(
  List.of(
    new Person("Peter", 33, "USA"),
    new Person("Brain", 10, "USA")
  ),
  List.of(
    new Person("Jack", 12, "UK"),
    new Person("Alex", 22, "USA")
  ),
  List.of(
    new Person("Steven", 24, "FR")
  )
);
```

现在想把它转换成单层流，用 flatMap 把它转换成单层流。flatMap 方法同样接受一个类型为 Function 的函数式接口作为参数，其中 T 代表原始流的元素类型，返回的是 Stream 流，R 表示的是新流的元素类型

```java 
Stream<List<Person>> peopleGroupsStream = peopleGroups.stream();
Stream<Person> peopleStream = peopleGroupsStream.flatMap(people -> people.stream());
peopleStream.forEach(System.out::println);
```

这边的 people 就是原始流的元素类型，类型为 `List <Person>`，返回的是转换后的新元素，类型为 `Stream<Person>`。flatMap 会通过这个函数对原始流中的每个元素做相同的处理，生成多个 Stream 流，最后将这些流合并成一个单一的扁平化的流。

现在流中的元素为 Person，如果我们想让流中的元素转化为人名，可以再用 map 来处理

```java
Stream<List<Person>> peopleGroupsStream = peopleGroups.stream();
Stream<Person> peopleStream = peopleGroupsStream.flatMap(Collection::stream);
Stream<String> nameStream = peopleStream.map(Person::getName);
nameStream.forEach(System.out::println);
```

另外需要注意的是，流的操作应该是链式调用的，即每次中间操作都应该基于前一次的操作结果，而不是重复使用同一个流实例进行多次操作。这是因为流操作返回的是一个新的流，而原始流在第一次操作后，就会被标记为已操作，不能再次进行操作。示例中将每个转换步骤赋值给变量，目的是为了清晰展示每次转换后流中元素的类型变换，方便理解，但实际应用中，我们通常会采用链式操作

```java
peopleGroups.stream()
  .flatMap(Collection::stream)
  .map(Person::getName)
  .forEach(System.out::println);
```

也可以这样写

```java
peopleGroups.stream()
  .flatMap(people -> people.stream().map(Person::getName))
  .forEach(System.out::println);
```

两者的区别是，前者首先用 faltMap 扁平化 Person 对象流，然后使用 map 将 Person 对象转化为 name，形成一个 name 流。后者是只使用 flatMap 在扁平化过程中直接进行转换，先将多个 Person 集合转换为多个 name 流，再合并为一个单一的 name 流。

当然我们也可以用 flatMap 实现 map 的功能，回到一维的例子

```java
List<Person> people = List.of(
  new Person("Peter", 33, "USA"),
  new Person("Brain", 10, "USA"),
  new Person("Jack", 12, "UK"),
  new Person("Alex", 22, "USA"),
  new Person("Steven", 24, "FR")
);
people.stream()
  .map(Person::getName)
  .forEach(System.out::println);
people.stream()
  .flatMap(person -> Stream.of(person.getName()))
  .forEach(System.out::println);
```

虽然此方法能实现 map 的功能，但实际上这种做法既复杂又影响性能，通常不推荐。这里只是为了方便理解 flatMap 的工作机制。

简单来说， map 用于元素一对一的转换，而 flatMap 不仅可以用于转换，还用于将多层的流合并为一个单层流。另外我们也可以通过 mapToInt、mapToLong、mapToDouble 等方法将流转换为对应的数值流

```java
IntStream ageStream = people.stream().mapToInt(Person::getAge);
ageStream.forEach(System.out::println);
```



### 排序

最后来看一下排序，排序包含了自然排序和自定义排序。当流中的元素类型实现了 Comparable 接口时，比如字符串或包装类型的数字 (如 Integer、Long 等等) 可以直接调用无参数的 sorted 方法安按照自然顺序进行排序

```java
Stream.of("blueberry", "strawberry", "apple", "peach", "pear")
  .sorted()
  .forEach(System.out::println);
```

也可以通过一个比较器 Comparator 来自定义排序规则，比如说按字符串长度从短到长排序

```java
Stream.of("blueberry", "strawberry", "apple", "peach", "pear")
  .sorted(Comparator.comparingInt(String::length))
  .forEach(System.out::println);
```

从长到短可以用 reversed 方法

```java
Stream.of("blueberry", "strawberry", "apple", "peach", "pear")
  .sorted(Comparator.comparingInt(String::length).reversed())
  .forEach(System.out::println);
```

对于自定义对象的流也可以使用这种方式进行排序

```java
people.stream()
  .sorted(Comparator.comparingInt(Person::getAge).reversed())
  .forEach(System.out::println);
```

接下来用一个综合应用的示例，来展示如何将 filter、map、sort 等这些中间操作结合起来，形成一个链式处理过程

初始数据

```java
List<List<Person>> peopleGroups = List.of(
  List.of(
    new Person("Peter", 33, "USA"),
    new Person("Brain", 10, "USA")
  ),
  List.of(
    new Person("Jack", 12, "UK"),
    new Person("Alex", 22, "USA"),
    new Person("Alex", 22, "USA")
  ),
  List.of(
    new Person("Steven", 24, "FR"),
    new Person("Steven", 24, "FR")
  )
);
```

流操作

```java
Stream<String> results = peopleGroups.stream()
  .flatMap(Collection::stream)
  .filter(person -> person.getAge() > 18)
  .distinct()
  .sorted(Comparator.comparingInt(Person::getAge).reversed())
  .map(Person::getName)
  .limit(3)
  .skip(1);
```

以上这些操作只是声明和定义了数据流的处理规则，并不会执行或产生结果，它们被成为中间操作。因此我们经常通过变量来保存和传递这些操作，以便在需要的时候触发执行。

要触发这些操作并获得结果，必须要执行一个终端操作，比如 forEach 来打印结果

```java
results.forEach(System.out::println);
```

如果没有这个终端操作的话，什么都不会执行。



## 终端操作

终端操作是流处理的最终步骤，一旦执行了终端操作，流的元素被消费，流就不能再被使用了。终端操作包括查找与匹配、聚合操作、规约操作、收集操作、迭代操作



### 查找与匹配 Search and Match

这类操作也属于短路操作 Short-circuiting Operations

之所以被称为短路操作，是因为当这些操作在找到符合条件的元素后会立即结束处理，返回结果，而不需要处理整个流，有效短路了流的遍历，提高了处理效率。特别使用在快速筛选或验证数据的场景中。

比方说我们想检查流中的元素是否满足某些条件，满足则返回 true，不满足则返回 false，就要用到 match 的一些方法

先来看一下 anyMatch，如果流中任意元素符合给定的条件就返回 true，参数是一个实现了 Predicate 接口的条件逻辑函数

```java
List<Person> people = List.of(
  new Person("Peter", 33, "USA"),
  new Person("Brain", 10, "USA"),
  new Person("Jack", 12, "UK"),
  new Person("Alex", 22, "USA"),
  new Person("Steven", 24, "FR")
);
boolean result = people.stream()
  .anyMatch(person -> person.getAge() > 18);
System.out.println(result);
```

与之相反的是 noneMatch，如果流中没有元素符合给定条件就返回 true

```java
boolean result = people.stream()
  .noneMatch(person -> person.getAge() > 50);
System.out.println(result);
```

还有 allMatch，它表示如果流中所有元素都符合给定条件才会返回 true

```java
boolean result = people.stream()
  .allMatch(person -> person.getAge() != 18);
System.out.println(result);
```



再来看一下搜索的相关方法

如果我们想从流中返回第一个元素就可以用 findFirst

```java
Optional<Person> optionalPerson = people.stream().findFirst();
optionalPerson.ifPresent(System.out::println);
```

返回的是 Optional 类型的对象。这样设计的原因是查询结果可能不存在，所以用 Optional 来封装更为安全

如果我们想从流中返回任意一个元素的话，我们就用 findAny，同样也是 Optional 返回的

```java
Optional<Person> optionalPerson = people.stream().findAny();
optionalPerson.ifPresent(System.out::println);
```



### 聚合操作 Aggregation Operation

聚合操作就是对流做一些基本统计常用的操作，比如求和、计算平均值、寻找最大值和最小值等等

先来统计一下流中的元素数量

```java
long count = people.stream().count();
System.out.println(count);
```

如果想找出流中最大的元素可以用 max，需要提供一个比较器来定义比较方法

```java
Optional<Person> optionalPerson = people.stream().max(Comparator.comparingInt(Person::getAge));
optionalPerson.ifPresent(System.out::println);
```

min 就是找出流中元素最小的那个

```java
Optional<Person> optionalPerson = people.stream().min(Comparator.comparingInt(Person::getAge));
optionalPerson.ifPresent(System.out::println);
```

我们还可以对流进行求和和计算平均值，但是这两个操作需要应用于基本类型的数值流，例如 IntStream、LongStream 和 DoubleStream

```java
IntStream ageStream = people.stream().mapToInt(Person::getAge);
int sum = ageStream.sum();
System.out.println(sum);
```

计算平均值

```java
IntStream ageStream = people.stream().mapToInt(Person::getAge);
OptionalDouble average = ageStream.average();
average.ifPresent(System.out::println);
```



### 规约操作 Reduction Operation

本质上聚合操作是规约操作 Reduction Operation 的一种特殊形式，适用于快捷简单的统计任务，比如求和、平均值、最大值和最小值等等

而规约操作 reduce 更为通用，它可以通过一个自定义的累加器函数，对流中的所有元素进行迭代处理，以累积出最终的结果。你可以实现任何类型的结果汇总，不仅限于数学上的聚合，而是任何形式的数据合并，比如拼接字符串、合并集合等等。

先用 reduce 来实现求和

```java
IntStream ageStream = people.stream().mapToInt(Person::getAge);
int sum = ageStream.reduce(0, (a, b) -> a + b);
System.out.println(sum);
```

如果想实现字符串的拼接，聚合操作里的方法就无法实现了，但是 reduce 就可以实现

比如把 name 通过 comma 逗号连接起来

```java
String joinedName = people.stream()
  .map(Person::getName)
  .reduce("", (a, b) -> a + b + ",");
System.out.println(joinedName);
```



### 收集操作 Collection Operation

接下来看一下收集操作 Collection Operation

这也是我们比较常用的操作，它允许我们把流中处理后的元素汇集到新的数据结构中，比如列表、集合或 Map 等等

比如把下面的数据放到新的 List 中

```java
List<Person> people = List.of(
  new Person("Peter", 33, "USA"),
  new Person("Brain", 10, "USA"),
  new Person("Jack", 12, "UK"),
  new Person("Alex", 22, "USA"),
  new Person("Steven", 24, "FR")
);
```

通过调用 collect 方法来收集数据，collect 方法要求我们提供一个定制的收集器 Collector，这个收集器是通过实现 Supplier Accumulator Combiner 和 Finisher 这几个关键的组件来创建的

为了简化收集器的构建，Java 引入了 Collectors 工具类，它提供了一系列静态方法，用于创建多种常用的收集器，例如 toList、toSet、toMap 等等

我们这边先用 Collectors.toList 收集到一个 List 集合中

```java
List<Person> adults = people.stream()
  .filter(person -> person.getAge() > 18)
  .collect(Collectors.toList());
System.out.println(adults);
```

也可以收集到 Map 集合中

```java
Map<String, Integer> adults = people.stream()
  .filter(person -> person.getAge() > 18)
  .collect(Collectors.toMap(
    Person::getName,
    Person::getAge
  ));
System.out.println(adults);
```

除此之外，Collectors 工具类还提供了许多实用的方法利润分组、分区、字符串连接以及各种统计功能等等

分组功能

按 Country 分组

```java
Map<String, List<Person>> peopleByCountry = people.stream()
  .collect(Collectors.groupingBy(Person::getCountry));
peopleByCountry.forEach((k, v) -> System.out.println(k + "=" + v));
```

按 age 是否大于 18 岁分组

```java
Map<Boolean, List<Person>> agePartition = people.stream()
  .collect(Collectors.groupingBy(person -> person.getAge() > 18));
agePartition.forEach((k, v) -> System.out.println(k + "=" + v));
```

使用 Collectors 提供的 joining 方法用 comma 逗号连接字符串

```java
String joinedName = people.stream()
  .map(Person::getName)
  .collect(Collectors.joining(","));
System.out.println(joinedName);
```

对年龄进行统计，包括元素数量、总和、最小值、平均值、最大值

```java
IntSummaryStatistics ageSummary = people.stream()
  .collect(Collectors.summarizingInt(Person::getAge));
System.out.println(ageSummary);
```



为了更好地理解 Collector 的收集过程和以上这些方法的实现机制，从而掌握并实现更为复杂的数据收集策略，我们将自定义一个 Collector 来模拟以上的一些功能，通过实现 Supplier Accumulator Combiner 以及 Finisher 这些方法

先来实现 toList 的功能

用 Collector 的 of 方法来创建这几个组件，先定义一个 Supplier 供应器，创建一个新的 List 实例作为数据容器，再定义一个 Accumulator 累加器，目的是将元素添加到 List 中。

接着我们创建一个 Combiner 组合器，它定义了如何合并两个并行流中的数据容器，命名为 left 和 right。Combiner 主要是用于并行流用来合并不同线程中供应器提供的数据收集容器。这边容器为 List，相对地在顺序流的处理中，比如本例，由于操作是单线程连续执行的，Supplier 提供的一个唯一的 List，累加器会逐步把元素添加到 List 中，最后作为结果返回，整个过程不需要合并，所以 Combiner 不会被调用，这本容器类型为 List，由 Supplier 提供。因为本例处理的是顺序流，所以这部分的代码不会执行。

最后我们来定义一下 Characteristics 特性，`Collector.Characteristics.IDENTITY_FINISH` 表明累加器的结果可以直接作为最终结果，无需通过额外的完成器 Finisher 进一步处理

```java
ArrayList<Person> collect = people.stream()
  .collect(Collector.of(
    () -> new ArrayList<>(),
    (list, person) -> {
      System.out.println("Accumulator: " + person);
      list.add(person);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left);
      left.addAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH
  ));
System.out.println(collect);
```

通过 Accumulator 多次执行，每一次添加一个元素，因为处理的是顺序流，所以这边的 Combiner 没有打印。现在把流转换为并行流再执行一下

```java
ArrayList<Person> collect = people.stream().parallel()
  .collect(Collector.of(
    () -> new ArrayList<>(),
    (list, person) -> {
      System.out.println("Accumulator: " + person);
      list.add(person);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left);
      left.addAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH
  ));
System.out.println(collect);
```

可以看到 Combiner 执行了，它执行合并过程，并且在不同的线程当中执行。



我们再来实现一下，按照国家对人员进行分组

我们先定义一个 Supplier，实例化一个 HashMap 作为数据容器，国家名称作为键，与之对应的人员列表作为值

我们再定义一个 Accumulator 将元素添加到 Map 中，添加规则是如果 Map 中已经存在以国家名称为键的列表，那么将 Person 对象添加到该列表中，如果不存在的话，将创建一个新的键值对，并立即将 Person 对象添加到新的列表中。

再定义一个 Combiner 用于并行流场景下合并操作，这里的目的是在并行流中合并两个不同线程中各自累加处理后的独立 Map 数据，比如这边合并的规则是以国家为键，将 right 中的列表添加到 left 列表中

```java
HashMap<String, List<Person>> collect = people.stream()
  .collect(Collector.of(
    HashMap::new,
    (map, person) -> {
      System.out.println("Accumulator: " + person);
      map.computeIfAbsent(person.getCountry(), k -> new ArrayList<>()).add(person);
    },
    (left, right) -> {
      System.out.println("Combiner: "
                         + System.lineSeparator() + "left: " + left
                         + System.lineSeparator() + "right: " + right
                        );
      right.forEach((k, v) -> left.merge(k, v, (list, newList) -> {
        list.addAll(newList);
        return list;
      }));
      return left;
    }
  ));
collect.forEach((k, v) -> System.out.println(k + " = " + v));
```

可以看到 Accumulator 里面依次添加数据。我们也可以看一下在并行流情况下的合并过程

```java
HashMap<String, List<Person>> collect = people.stream().parallel()
  .collect(Collector.of(
    HashMap::new,
    (map, person) -> {
      System.out.println("Accumulator: " + person);
      map.computeIfAbsent(person.getCountry(), k -> new ArrayList<>()).add(person);
    },
    (left, right) -> {
      System.out.println("Combiner: "
                         + System.lineSeparator() + "left: " + left
                         + System.lineSeparator() + "right: " + right
                        );
      right.forEach((k, v) -> left.merge(k, v, (list, newList) -> {
        list.addAll(newList);
        return list;
      }));
      return left;
    }
  ));
collect.forEach((k, v) -> System.out.println(k + " = " + v));
```

可以看到合并在不同线程中进行。这里 `Characteristics` 不加默认就是 `IDENTITY_FINISH`，也可以自定义一个 Finisher 对累加器的结果进一步处理，比如计算结果元素个数，这个时候就不需要 `IDENTITY_FINISH` 了，额外加一个 Finisher，如 `HashMap::size`，不过这样编译器就无法推断出具体类型，所以 Supplier 里面需要明确指定 HashMap 的类型

```java
int size = people.stream().parallel()
  .collect(Collector.of(
    HashMap<String, List<Person>>::new,
    (map, person) -> {
      System.out.println("Accumulator: " + person);
      map.computeIfAbsent(person.getCountry(), k -> new ArrayList<>()).add(person);
    },
    (left, right) -> {
      System.out.println("Combiner: "
                         + System.lineSeparator() + "left: " + left
                         + System.lineSeparator() + "right: " + right
                        );
      right.forEach((k, v) -> left.merge(k, v, (list, newList) -> {
        list.addAll(newList);
        return list;
      }));
      return left;
    },
    HashMap::size
  ));
System.out.println(size);
```

对于某些场景，我们不需要返回任何结果，只需要对流中的每个元素执行某些操作。这时我们可以使用迭代操作 forEach 方法，它会遍历流中的每个元素并执行指定的操作，常用于触发副作用，例如打印元素。方法需要一个 Consumer 函数式接口作为参数

```java
people.stream()
  .forEach(System.out::println);
```



## 并行流

到这里，大家应该已经对流的具体使用方法有了一定的了解，另外目前展示的所有示例都是基于顺序流的操作，它是单线程顺序执行的，Stream API 还提供了一种更高效的解决方案，那就是并行流 Parallel Streams，它能够借助多核处理器的并行计算能力加速数据处理，特别适合大型数据集或计算密集型任务。

其核心工作原理是这样的：并行流在开始时， Spliterator 分割迭代器会将数据分割成多个片段，分割过程通常采用递归的方式动态进行，以平衡子任务的工作负载，提高资源利用率，然后 Fork/Join 框架 会将这些数据片段分配到多个线程和处理器核心上进行并行处理。处理完成后，结果将会被汇总合并。其核心是任务的分解 Fork 和结果的合并 Join，在操作上，无论是并行流还是顺序流，两者都提供相同的中间操作和终端操作，这意味着你可以用几乎相同的方式进行数据处理和结果收集。

我们先来创建一个由大写字母组成的 List，有两种方法创建并行流，可以先创建一个顺序流然后再通过调用 parallel 方法来创建，或者因为这里是 List，可以直接使用 parallelStream 来创建

```java
List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .forEach(System.out::println);
```

操作上和顺序流没有什么差别，然而在使用并行流时，我们需要注意，并行流不像顺序流的执行是单线程的按照元素在流中的顺序依次执行，并行流采用多线程并发处理，可以同时在多个核心上处理数据，但不保证元素的处理顺序，除非系统做特殊的处理。比如我们可以用 forEachOrdered 来保持原始排列顺序，通常称之为出现顺序 Encounter Order。

```java
List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .forEachOrdered(System.out::println);
```

之所以能够保持这个出现顺序，这归功于 Spliterator 和 Fork/Join 框架的协作。过程是这样的，在处理并行流时，对于有序数据源比如 List，Spliterator 会对数据源进行递归分割，分割通常是基于逻辑上的而非物理上的复制数据，通过划分数据源的索引范围来实现。每次分割都会产生一个新的 Spliterator 实例，该实例内部维护了指向原数据的索引范围。这种分割机制可以让数据的出现顺序得以保持，然后 Fork/Join 框架接手，将分配后的数据块分配给不同的子任务执行。

对于 forEachOrdered 的操作，Fork/Join 框架依据 Spliterator 维护的顺序信息来调度方法的执行顺序，这意味着即使某个子任务较早完成，如果其关联的方法执行顺序还未到来，那么系统将缓存数据并暂停执行该方法，直到所有前续任务都已完成并执行了各自的相关方法。这种机制确保了即使在并行处理的情况下，每个操作也会按照原始数据的出现顺序执行。当然这么做也可能会牺牲一些并行执行的效率。另外值得注意的是 forEachOrdered 只专注于按出现顺序执行指定操作，不需要对数据进行合并和收集。

对于 forEach 操作，尽管 Spliterator 分割策略相同，依旧保留着顺序信息，但 Fork/Join 框架在执行时会忽略这些顺序信息，因此执行不保证遵循原始顺序，但能够提供更高的执行效率。forEach 操作会在不同的线程上独立执行，这意味着如果操作的是共享资源，那么必须确保这些操作是线程安全的，如果没有适当的同步措施，可能会引发线程安全问题。因此在并行流中，forEach 更适合执行无状态的操作或处理资源独立的场景。

```java
List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .forEach(item -> {
    System.out.println("Item: " + item + " -> " + " Thread: " + Thread.currentThread().getName());
    System.out.println(item);
  });
```

接下来看一下，并行流在收集数据时的顺序问题，比如我们将数据收集到列表 List 中。

```java
List<String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collectors.toList());
System.out.println(collect);
```

执行打印的顺序是没问题的。其实整个处理流程和 forEachOrdered 操作很相似，尤其是在数据的分割和并行处理阶段。主要的不同在于 collect 操作需要在最终合并收集数据，而 forEachOrdered 不需要。具体来说，每个并行执行的任务在完成处理后会将其结果存储到一个临时数据结构中，Fork/Join 框架会利用 Spliterator 提供的区段顺序信息引导这些临时结果按顺序的合并。同样，即使某个逻辑上靠后的数据段先处理完成，合并时也不会让这个结果前置。整个合并过程递归进行，直至所有的结果合并完毕。

观察一下内部的执行方式，先用一个自定义的收集器来实现一下 toList 方法。

定义一个 Supplier 供应器，创建一个新的 List 实例作为数据容器

再定义一个 Accumulator 累加器，将元素添加到 list 中

接下来再定义一个组合器，这是并行流与顺序流处理的主要区别之一，顺序流不需要合并步骤，因为它在单线程中连续处理，对于并行流而言，如何有效地合并不同线程的处理结果，是一个重要的环节。left 和 right 分别代表相邻的两个数据段的处理结果，一般会在不同线程中出现，按出现顺序排列，left 为前，right 为后，即使 right 部分先完成，依旧在 right 的位置会等待 left 的完成

最后再定义一个 Characteristics 特性，指定为默认完成器，就是一个恒等函数，它会将累加器的结果作为最终结果

```java
List<String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new ArrayList" + " Thread: " + Thread.currentThread().getName());
      return new ArrayList<>();
    },
    (list, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      list.add(item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.addAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH
  ));
System.out.println(collect);
```

结果是有顺序的。看一下过程，先在不同的线程里实例化容器，在 Supplier 这边然后转换元素为小写并累加到局部的 list，这个是在 Accumulator 里面完成的。left right 合并操作，虽然它不是在同一个线程执行，但仍然按照逻辑上先后顺序依次组合。

当我们将收集操作从 toList 改为 toSet，结果就会呈现无序

```java
Set<String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collectors.toSet());
```

这是因为目标数据 Set 数据结构的性质导致的，而并不是并行处理本身导致的。需要强调的是，在这里无论是采用 toList 还是 toSet 收集数据，整个处理过程包括 Spliterator 的分割策略，Fork/Join 框架的执行和合并策略都没变。因为这些策略是由数据源的性质决定的，而数据源是同一个 List。唯一不同的是，合并前存储局部结果的临时数据结构，一个是 List，一个是 Set

将前面的代码改造为 HashSet 存储

```java
Set<String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new HashSet" + " Thread: " + Thread.currentThread().getName());
      return new HashSet<>();
    },
    (list, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      list.add(item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.addAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH
  ));
System.out.println(collect);
```

结果是乱序的，这符合 Set 的规律。但是框架在合并时的 left 位置的数据块依旧在 right 的前面，分割和合并的策略依旧没变。

另外关于 Characteristics 中的 UNORDERED 和 CONCURRENT 两个特性，也经常会引起一些误解和混淆。特别是 UNORDERED，许多人可能会误以为一旦设置了 UNORDERED，处理的结果就会呈现为无序状态，然而实际上结果往往仍然是有序的

```java
List<String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new ArrayList" + " Thread: " + Thread.currentThread().getName());
      return new ArrayList<>();
    },
    (list, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      list.add(item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.addAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH,
    Collector.Characteristics.UNORDERED
  ));
System.out.println(collect);
```

即便加了 UNORDERED，这里的结果顺序依旧按照出现顺序来。这是因为即便 Collector 被标记为 UNORDERED，如果数据源或流操作本身是有序的，系统的执行策略通常仍会保持这些元素的出现顺序，这实际上也是符合逻辑的。只有在特定场景下，系统才会针对那些被标记为 UNORDERED 的流进行优化，从而打破顺序约束。

还有就是 CONCURRENT 特性。在标准的并行流处理中，每个线程处理数据的一个子集，维护自己的局部结果容器。在所有的数据处理完成后，这些局部结果会通过一个 Combiner 的函数合并成一个最终结果。使用 CONCURRENT 特性后，所有线程将共享同一个结果容器，而不是维护独立的局部结果，从而减少了合并的需要。这通常会带来性能上的提升，特别是当结果容器较大或合并操作较为复杂时，这也就意味着供应函数只会被调用一次，只创建一个结果容器，而且这个容器必须是线程安全的，例如 ConcurrentHashMap，此外合并函数将不会再执行。如果使用的不是线程安全的数据容器，系统就会忽略这个特性。

```java
ConcurrentHashMap<String, String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new ConcurrentHashMap" + " Thread: " + Thread.currentThread().getName());
      return new ConcurrentHashMap<>();
    },
    (map, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      map.put(item.toUpperCase(), item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.putAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH,
    Collector.Characteristics.CONCURRENT
  ));
System.out.println(collect);
```

这里 Supplier 函数仍被多次调用，依旧会按照默认策略在多个线程中创建 ConcurrentHashMap 实例。这是因为在处理有序流的情况下，如果多个线程并发更新同一个共享的累加器容器，那么元素更新的顺序将变得不确定。为了避免这种情况，框架通常会忽略有序源的 CONCURRENT 特性，除非同时还指定了 UNORDERED 特性

```java
ConcurrentHashMap<String, String> collect = List.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new ConcurrentHashMap" + " Thread: " + Thread.currentThread().getName());
      return new ConcurrentHashMap<>();
    },
    (map, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      map.put(item.toUpperCase(), item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.putAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH,
    Collector.Characteristics.CONCURRENT,
    Collector.Characteristics.UNORDERED
  ));
System.out.println(collect);
```

这里 Supplier 只执行了一次，只 new 一个 ConcurrentHashMap。Accumulator 也执行了，而 Combiner 就没有执行。这意味着 CONCURRENT 特性生效了

如果删除 UNORDERED 特性，把 List 改为 Set

```java
ConcurrentHashMap<String, String> collect = Set.of("E", "N", "T", "R", "O", "P", "Y").parallelStream()
  .map(String::toLowerCase)
  .collect(Collector.of(
    () -> {
      System.out.println("Supplier: new ConcurrentHashMap" + " Thread: " + Thread.currentThread().getName());
      return new ConcurrentHashMap<>();
    },
    (map, item) -> {
      System.out.println("Accumulator: " + item + " Thread: " + Thread.currentThread().getName());
      map.put(item.toUpperCase(), item);
    },
    (left, right) -> {
      System.out.println("Combiner: " + left + " + " + right + " Thread: " + Thread.currentThread().getName());
      left.putAll(right);
      return left;
    },
    Collector.Characteristics.IDENTITY_FINISH,
    Collector.Characteristics.CONCURRENT
  ));
System.out.println(collect);
```

结果是类似的，也只实例化一个容器，CONCURRENT 特性还是生效的。所以主要还是因为有序源导致的这个现象。



很多时候我们经常会担心使用并行流时，是否会导致与顺序流在结果上的不一致以及哪些情况可能会引起这种差异。实际上对于并行流，通过系统内部精确的执行策略，绝大多数的终端操作都能够产生与顺序流一致的结果。

比如 count、max、min、sum、average 这些聚合操作，并不依赖于元素的出现顺序，只需要将各个子任务的计算结果合并到最终结果。系统的  是先分片计算再合并计算。

还有比如 anyMatch、allMatch、noneMatch、findFirst、findAny 这些短路操作，不仅能够保证结果的一致性，而且在某些情况下，还显著提高了执行效率。当另一个子任务发现其处理的元素已满足或不满足给定条件时，它将立即终止处理、返回结果，同时通知其他尚未完成的子任务停止执行，即便是涉及 distinct 和 sorted 这两个有状态的中间操作，也不影响最终结果的一致性。系统会对每个分片的任务结果进行单独排序或去重，然后在合并结果的过程中，再次进行排序或去重处理。依次类推，通过多次合并处理，完成最终的排序和去重。不过这种方式也额外带来了一定的性能开销。

然而，并非所有的操作都能保证一致的结果，比如 forEach 和某些形式的 reduce。reduce 是否能保持一致性取决于使用的操作是否关联，如果操作不是关联的，结果可能就不一致。

比如用 reduce 创建一个累加器

```java
Integer sum = List.of(1, 2, 3, 4).parallelStream()
  .reduce(0, (a, b) -> a + b);
System.out.println(sum);
```

这里 a+b 是一个关联操作，因为加法满足交换律和结合律，即 a+b = b+a，因此 reduce 的结果符合预期。类似的乘法、最大值、最小值运算也都是关联的，无论在并行流还是在顺序流中，结果都会一致。

如果是减法，由于不满足结合律，即 a-b != b-a，它不是一个关联操作，所以结果肯定会不一致。

```java
Integer res = List.of(1, 2, 3, 4).parallelStream()
  .reduce(0, (a, b) -> {
    System.out.println(a + " - " + b + " Thread: " + Thread.currentThread().getName());
    return a - b;
  });
System.out.println(res);
```

这里 1 2 3 4 分别在四个线程里面执行，结果不符合预期，这是减法操作本身的非关联性导致的。



总而言之，并行流能够充分发挥多核处理的优势，特别适合处理大数据量和计算密集型任务。然而对于数据量规模较小或涉及 IO 操作的情况，顺序流可能会更合适。这是因为并行处理涉及线程管理和协调的额外开销，这些开销可能会抵消甚至超过了并行执行带来的性能提升，所以在决定是否使用并行流时，应该先评估任务的性质、数据的规模以及预期的性能收益，以做出最合适的选择。



# 致谢

参考内容来源于 [Java中的流、并行流 - Java Stream API | Parallel Streams](https://www.bilibili.com/video/BV1Vi421C73n)
