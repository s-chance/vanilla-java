package io.github.schance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTests {

    @Test
    public void arrayListTest() {
        List<String> list = new ArrayList<>();
        operateTest(list);
    }

    @Test
    public void linkedListTest() {
        List<String> list = new LinkedList<>();
        operateTest(list);
    }

    public void operateTest(List list) {
        for (int i = 0; i < 30; i++) {
            list.add(String.valueOf(i));
        }
        assertEquals(30, list.size());
        list.remove(15);
        list.remove("18");
        assertEquals(28, list.size());
        assertEquals("16", list.get(15));
        assertEquals("24", list.get(22));

        list.forEach(System.out::println);
    }
}
