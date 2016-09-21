package com.rareventure.quietcraft;

import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Generic utilities
 */
public class Util {
    /**
     * If object is of type class, will run function on it, otherwise returns default
     * @param c class
     * @param o object
     * @param f function
     * @param d default
     * @param <T>
     * @return
     */
    public static <T> Object castAndCall(Class<T> c, Object o, Function<T, ?> f, Object d) {
        if(c.isInstance(o))
        {
            return f.apply((T)o);
        }
        return d;
    }
}
