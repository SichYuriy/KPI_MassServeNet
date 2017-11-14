package modeling.lab4.util;

import lombok.AllArgsConstructor;

@lombok.Data
@AllArgsConstructor
public class Pair<K, V> {
    private K key;
    private V value;
}
