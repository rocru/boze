package netutil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class uselessloaderclass2<K, V> extends HashMap<K, V> {
    public uselessloaderclass2(final int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public int size() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return 0;
            }
        }
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public V get(final Object key) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.get(key);
    }

    @Override
    public boolean containsKey(final Object key) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return false;
            }
        }
        return super.containsKey(key);
    }

    @Override
    public V put(final K key, final V value) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return value;
            }
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return;
            }
        }
        super.putAll(m);
    }

    @Override
    public V remove(final Object key) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.remove(key);
    }

    @Override
    public void clear() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return;
            }
        }
        super.clear();
    }

    @Override
    public boolean containsValue(final Object value) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return false;
            }
        }
        return super.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.entrySet();
    }

    @Override
    public V getOrDefault(final Object key, final V defaultValue) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return defaultValue;
            }
        }
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return value;
            }
        }
        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return false;
            }
        }
        return super.remove(key, value);
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return false;
            }
        }
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(final K key, final V value) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return value;
            }
        }
        return super.replace(key, value);
    }

    @Override
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.compute(key, remappingFunction);
    }

    @Override
    public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.merge(key, value, remappingFunction);
    }

    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return;
            }
        }
        super.forEach(action);
    }

    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return;
            }
        }
        super.replaceAll(function);
    }

    @Override
    public Object clone() {
        final Field[] declaredFields = BozeMixins.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            if (!Modifier.isPrivate(declaredFields[i].getModifiers())) {
                return null;
            }
        }
        return super.clone();
    }
}
