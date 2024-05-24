package net.atticus.combat_utilities.util;

public interface Consumer<T, U> {
    void accept(T t, U u);
}
