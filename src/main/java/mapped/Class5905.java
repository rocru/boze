package mapped;

import netutil.BozeClassLoader;
import netutil.Count;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Class5905<K, V> extends HashMap<K, V> {
    public Class5905(final int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public int size() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final int n;
                final boolean b2 = (n = (Modifier.isPrivate(field4013.getModifiers()) ? 1 : 0)) != 0;
                if (b) {
                    return n;
                }
                if (!b2) {
                    return 0;
                }
                ++i;
            }
            if (b) {
                break;
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
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object value;
                final Field field4014 = (Field) (value = field4013);
                if (b) {
                    return (V) value;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object value = super.get(key);
        return (V) value;
    }

    @Override
    public boolean containsKey(final Object key) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean field4012 = Count.field4012;
        final int length = declaredFields.length;
        int i = 0;
        final boolean b = field4012;
        boolean private1 = false;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                private1 = Modifier.isPrivate(field4013.getModifiers());
                if (!private1) {
                    return false;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.containsKey(key);
        return private1;
    }

    @Override
    public V put(final K key, final V value) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object put;
                final Field field4014 = (Field) (put = field4013);
                if (b) {
                    return (V) put;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return value;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object put = super.put(key, value);
        return (V) put;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final int length = declaredFields.length;
        final boolean field4012 = Count.field4012;
        int i = 0;
        final boolean b = field4012;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (b) {
                return;
            }
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.putAll(m);
    }

    @Override
    public V remove(final Object key) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final int length = declaredFields.length;
        int i = 0;
        final boolean b = field4012;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object remove;
                final Field field4014 = (Field) (remove = field4013);
                if (b) {
                    return (V) remove;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object remove = super.remove(key);
        return (V) remove;
    }

    @Override
    public void clear() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (b) {
                return;
            }
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.clear();
    }

    @Override
    public boolean containsValue(final Object value) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final int length = declaredFields.length;
        final boolean b = field4012;
        int i = 0;
        boolean private1 = false;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                private1 = Modifier.isPrivate(field4013.getModifiers());
                if (b) {
                    return private1;
                }
                if (!private1) {
                    return false;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.containsValue(value);
        return private1;
    }

    @Override
    public Set<K> keySet() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        return super.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        return super.entrySet();
    }

    @Override
    public V getOrDefault(final Object key, final V defaultValue) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean field4012 = Count.field4012;
        final int length = declaredFields.length;
        int i = 0;
        final boolean b = field4012;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object orDefault;
                final Field field4014 = (Field) (orDefault = field4013);
                if (b) {
                    return (V) orDefault;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return defaultValue;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object orDefault = super.getOrDefault(key, defaultValue);
        return (V) orDefault;
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final int length = declaredFields.length;
        final boolean b = field4012;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object putIfAbsent;
                final Field field4014 = (Field) (putIfAbsent = field4013);
                if (b) {
                    return (V) putIfAbsent;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return value;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object putIfAbsent = super.putIfAbsent(key, value);
        return (V) putIfAbsent;
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        boolean private1 = false;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                private1 = Modifier.isPrivate(field4013.getModifiers());
                if (b) {
                    return private1;
                }
                if (!private1) {
                    return false;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.remove(key, value);
        return private1;
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        boolean private1 = false;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                private1 = Modifier.isPrivate(field4013.getModifiers());
                if (b) {
                    return private1;
                }
                if (!private1) {
                    return false;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.replace(key, oldValue, newValue);
        return private1;
    }

    @Override
    public V replace(final K key, final V value) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object replace;
                final Field field4014 = (Field) (replace = field4013);
                if (b) {
                    return (V) replace;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return value;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object replace = super.replace(key, value);
        return (V) replace;
    }

    @Override
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean field4012 = Count.field4012;
        final int length = declaredFields.length;
        final boolean b = field4012;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object computeIfAbsent;
                final Field field4014 = (Field) (computeIfAbsent = field4013);
                if (b) {
                    return (V) computeIfAbsent;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object computeIfAbsent = super.computeIfAbsent(key, mappingFunction);
        return (V) computeIfAbsent;
    }

    @Override
    public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object computeIfPresent;
                final Field field4014 = (Field) (computeIfPresent = field4013);
                if (b) {
                    return (V) computeIfPresent;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object computeIfPresent = super.computeIfPresent(key, remappingFunction);
        return (V) computeIfPresent;
    }

    @Override
    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean field4012 = Count.field4012;
        final int length = declaredFields.length;
        final boolean b = field4012;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object compute;
                final Field field4014 = (Field) (compute = field4013);
                if (b) {
                    return (V) compute;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object compute = super.compute(key, remappingFunction);
        return (V) compute;
    }

    @Override
    public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean field4012 = Count.field4012;
        final int length = declaredFields.length;
        int i = 0;
        final boolean b = field4012;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object merge;
                final Field field4014 = (Field) (merge = field4013);
                if (b) {
                    return (V) merge;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        Object merge = super.merge(key, value, remappingFunction);
        return (V) merge;
    }

    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (b) {
                return;
            }
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.forEach(action);
    }

    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final int length = declaredFields.length;
        final boolean b = field4012;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (b) {
                return;
            }
            if (!b) {
                if (!Modifier.isPrivate(field4013.getModifiers())) {
                    return;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        super.replaceAll(function);
    }

    @Override
    public Object clone() {
        final boolean field4012 = Count.field4012;
        final Field[] declaredFields = BozeClassLoader.class.getDeclaredFields();
        final boolean b = field4012;
        final int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            final Field field4013 = declaredFields[i];
            if (!b) {
                final Object clone;
                final Field field4014 = (Field) (clone = field4013);
                if (b) {
                    return clone;
                }
                if (!Modifier.isPrivate(field4014.getModifiers())) {
                    return null;
                }
                ++i;
            }
            if (b) {
                break;
            }
        }
        return super.clone();
    }
}
