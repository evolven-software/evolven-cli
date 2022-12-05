package com.evolven.common;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Helper functions for manipulating {@link Consumer}. */
public class Consumers {
    /** A Consumer which does nothing. */
    public static <T> Consumer<T> doNothing() {
        return value -> {};
    }

    /** The equivalent of {@link Function#compose}, but for Consumer. */
    public static <T, R> Consumer<T> compose(Function<? super T, ? extends R> function, Consumer<? super R> consumer) {
        return value -> consumer.accept(function.apply(value));
    }

    /**
     * A Consumer which always passes its its input to whatever Consumer is supplied by target.
     * <p>
     * By passing something mutable, such as a {@link Box}, you can redirect the returned consumer.
     */
    public static <T> Consumer<T> redirectable(Supplier<Consumer<T>> target) {
        return value -> target.get().accept(value);
    }
}
