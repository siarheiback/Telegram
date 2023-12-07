package org.telegram.bautrukevich;

public interface GenericProvider<F, T> {
    T provide(F obj);
}
