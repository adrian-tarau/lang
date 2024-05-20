package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    void asCollection() {
        assertIterableEquals(List.of("I", "am", "writing", "java", "code"),
                CollectionUtils.asCollection(List.of("I", "am", "writing", "java", "code")));
        assertIterableEquals(Collections.EMPTY_LIST, CollectionUtils.asCollection((Collection<Object>) null));
    }

    @Test
    void testAsCollection() {
        assertIterableEquals(Map.of("item1", 1, "item2", 2, "item3", 3).values(),
                CollectionUtils.asCollection(Map.of("item1", 1, "item2", 2, "item3", 3)));
        assertIterableEquals(Collections.EMPTY_LIST, CollectionUtils.asCollection((Map<Object, Object>) null));
    }

    @Test
    void asSet() {
        assertIterableEquals(Set.of(1, 2, 3, 4, 5),
                CollectionUtils.asSet(Set.of(1, 2, 3, 4, 5)));
        assertIterableEquals(Collections.EMPTY_SET, CollectionUtils.asSet(null));
    }

    @Test
    void asMap() {
        assertEquals(Map.of("item1", 1, "item2", 2, "item3", 3),
                CollectionUtils.asMap(Map.of("item1", 1, "item2", 2, "item3", 3)));
        assertEquals(Collections.EMPTY_MAP, CollectionUtils.asMap(null));
    }

    @Test
    void immutableCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("I");
        strings.add("writing");
        strings.add("java");
        strings.add("code");
        assertThrows(UnsupportedOperationException.class, () ->
                CollectionUtils.immutableCollection(strings).remove("code"));
        assertIterableEquals(Collections.EMPTY_LIST, CollectionUtils.immutableCollection(null));
    }

    @Test
    void immutableSet() {
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(5);
        assertThrows(UnsupportedOperationException.class, () ->
                CollectionUtils.immutableSet(integers).remove(2));
        assertIterableEquals(Collections.EMPTY_SET, CollectionUtils.immutableSet(null));
    }

    @Test
    void immutableMap() {
        Map<String,Integer> integerMap= new HashMap<>();
        integerMap.put("item1",1);
        integerMap.put("item2",2);
        integerMap.put("item3",3);
        assertThrows(UnsupportedOperationException.class, () ->
                CollectionUtils.immutableMap(integerMap).remove("item1"));
        assertEquals(Collections.EMPTY_MAP, CollectionUtils.immutableMap(null));
    }

    @Test
    void toListWithIterator() {
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(5);
        assertIterableEquals(List.of(1,2,3,4,5),CollectionUtils.toList(integers.iterator()));
        assertEquals(Collections.EMPTY_LIST,CollectionUtils.toList((Iterator<Object>) null));
    }

    @Test
    void toListWithIterable() {
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(5);
        assertIterableEquals(List.of(1,2,3,4,5),CollectionUtils.toList(integers));
    }

    @Test
    void toIterable() {
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(5);
        assertIterableEquals(List.of(1,2,3,4,5),CollectionUtils.toIterable(integers.iterator()));
        assertEquals(Collections.EMPTY_LIST,CollectionUtils.toIterable(null));
    }
}