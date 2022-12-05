/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolven.common;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Executes code and wraps functions, sending any errors to a {@code Consumer<Throwable>} error handler,
 * see <a href="https://github.com/diffplug/durian/blob/master/test/com/diffplug/common/base/ErrorsExample.java">ErrorsExample</a>.
 */
public abstract class Errors implements Consumer<Throwable> {

    protected final Consumer<Throwable> handler;

    protected Errors(Consumer<Throwable> error) {
        this.handler = error;
    }

    /**
     * Creates an Errors.Handling which passes any exceptions it receives
     * to the given handler.
     * <p>
     * The handler is free to throw a RuntimeException if it wants to. If it always
     * throws a RuntimeException, then you should instead create an Errors.Rethrowing
     * using {@link #createRethrowing}.
     */
    public static Handling createHandling(Consumer<Throwable> handler) {
        return new Handling(handler);
    }

    /**
     * Creates an Errors.Rethrowing which transforms any exceptions it receives into a RuntimeException
     * as specified by the given function, and then throws that RuntimeException.
     * <p>
     * If that function happens to throw an unchecked error itself, that'll work just fine too.
     */
    public static Rethrowing createRethrowing(Function<Throwable, RuntimeException> transform) {
        return new Rethrowing(transform);
    }

    /** Suppresses errors entirely. */
    public static Handling suppress() {
        return suppress;
    }

    private static final Handling suppress = createHandling(Consumers.doNothing());

    /** Rethrows any exceptions as runtime exceptions. */
    public static Rethrowing rethrow() {
        return rethrow;
    }

    private static final Rethrowing rethrow = createRethrowing(Errors::rethrowErrorAndWrapOthersAsRuntime);

    private static RuntimeException rethrowErrorAndWrapOthersAsRuntime(Throwable e) {
        if (e instanceof Error) {
            throw (Error) e;
        } else {
            return Errors.asRuntime(e);
        }
    }

    /** Passes the given error to this Errors. */
    @Override
    public void accept(Throwable error) {
        handler.accept(error);
    }

    /** Converts this {@code Consumer<Throwable>} to a {@code Consumer<Optional<Throwable>>}. */
    public Consumer<Optional<Throwable>> asTerminal() {
        return errorOpt -> {
            if (errorOpt.isPresent()) {
                accept(errorOpt.get());
            }
        };
    }

    /** Attempts to run the given runnable. */
    public void run(Throwing.Runnable runnable) {
        wrap(runnable).run();
    }

    /** Returns a Runnable whose exceptions are handled by this Errors. */
    public Runnable wrap(Throwing.Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    /** Returns a Consumer whose exceptions are handled by this Errors. */
    public <T> Consumer<T> wrap(Throwing.Consumer<T> consumer) {
        return val -> {
            try {
                consumer.accept(val);
            } catch (Throwable e) {
                handler.accept(e);
            }
        };
    }

    /**
     * An {@link Errors} which is free to rethrow the exception, but it might not.
     * <p>
     * If we want to wrap a method with a return value, since the handler might
     * not throw an exception, we need a default value to return.
     */
    public static class Handling extends Errors {
        protected Handling(Consumer<Throwable> error) {
            super(error);
        }

        /** Attempts to call {@code supplier} and returns {@code onFailure} if an exception is thrown. */
        public <T> T getWithDefault(Throwing.Supplier<T> supplier, T onFailure) {
            return wrapWithDefault(supplier, onFailure).get();
        }

        /** Returns a Supplier which wraps {@code supplier} and returns {@code onFailure} if an exception is thrown. */
        public <T> Supplier<T> wrapWithDefault(Throwing.Supplier<T> supplier, T onFailure) {
            return () -> {
                try {
                    return supplier.get();
                } catch (Throwable e) {
                    handler.accept(e);
                    return onFailure;
                }
            };
        }

        public <T, R> Function<T, R> wrapWithDefault(Throwing.Function<T, R> function, R onFailure) {
            return wrapFunctionWithDefault(function, onFailure);
        }

        public <T> Predicate<T> wrapWithDefault(Throwing.Predicate<T> predicate, boolean onFailure) {
            return wrapPredicateWithDefault(predicate, onFailure);
        }

        /** Returns a Function which wraps {@code function} and returns {@code onFailure} if an exception is thrown. */
        public <T, R> Function<T, R> wrapFunctionWithDefault(Throwing.Function<T, R> function, R onFailure) {
            return input -> {
                try {
                    return function.apply(input);
                } catch (Throwable e) {
                    handler.accept(e);
                    return onFailure;
                }
            };
        }

        /** Returns a Predicate which wraps {@code predicate} and returns {@code onFailure} if an exception is thrown. */
        public <T> Predicate<T> wrapPredicateWithDefault(Throwing.Predicate<T> predicate, boolean onFailure) {
            return input -> {
                try {
                    return predicate.test(input);
                } catch (Throwable e) {
                    handler.accept(e);
                    return onFailure;
                }
            };
        }
    }

    /**
     * An {@link Errors} which is guaranteed to always throw a RuntimeException.
     * <p>
     * If we want to wrap a method with a return value, it's pointless to specify
     * a default value because if the wrapped method fails, a RuntimeException is
     * guaranteed to throw.
     */
    public static class Rethrowing extends Errors {
        private final Function<Throwable, RuntimeException> transform;

        protected Rethrowing(Function<Throwable, RuntimeException> transform) {
            super(error -> {
                throw transform.apply(error);
            });
            this.transform = transform;
        }

        /** Attempts to call {@code supplier} and rethrows any exceptions as unchecked exceptions. */
        public <T> T get(Throwing.Supplier<T> supplier) {
            return wrap(supplier).get();
        }

        /** Returns a Supplier which wraps {@code supplier} and rethrows any exceptions as unchecked exceptions. */
        public <T> Supplier<T> wrap(Throwing.Supplier<T> supplier) {
            return () -> {
                try {
                    return supplier.get();
                } catch (Throwable e) {
                    throw transform.apply(e);
                }
            };
        }

      //  public <T, R> Function<T, R> wrap(Throwing.Function<T, R> function) {
      //      return wrapFunction(function);
      //  }

      //  public <T> Predicate<T> wrap(Throwing.Predicate<T> predicate) {
      //      return wrapPredicate(predicate);
      //  }

        /** Returns a Function which wraps {@code function} and rethrows any exceptions as unchecked exceptions. */
        public <T, R> Function<T, R> wrapFunction(Throwing.Function<T, R> function) {
            return arg -> {
                try {
                    return function.apply(arg);
                } catch (Throwable e) {
                    throw transform.apply(e);
                }
            };
        }

        /** Returns a Predicate which wraps {@code predicate} and rethrows any exceptions as unchecked exceptions. */
        public <T> Predicate<T> wrapPredicate(Throwing.Predicate<T> predicate) {
            return arg -> {
                try {
                    return predicate.test(arg);
                } catch (Throwable e) {
                    throw transform.apply(e); // 1 855 548 2505
                }
            };
        }
    }

    /** Casts or wraps the given exception to be a RuntimeException. */
    public static RuntimeException asRuntime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new WrappedAsRuntimeException(e);
        }
    }

    /** A RuntimeException specifically for the purpose of wrapping non-runtime Throwables as RuntimeExceptions. */
    public static class WrappedAsRuntimeException extends RuntimeException {
        private static final long serialVersionUID = -912202209702586994L;

        public WrappedAsRuntimeException(Throwable e) {
            super(e);
        }
    }
}
