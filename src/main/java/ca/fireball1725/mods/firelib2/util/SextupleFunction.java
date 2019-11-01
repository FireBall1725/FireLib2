package ca.fireball1725.mods.firelib2.util;

import java.util.Objects;
import java.util.function.Function;

public interface SextupleFunction<A, B, C, D, E, F, R> {
  R apply(A a, B b, C c, D d, E e, F f);

  default <V> SextupleFunction<A, B, C, D, E, F, V> andThen(
    Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (A a, B b, C c, D d, E e, F f) -> after.apply(apply(a, b, c, d, e, f));
  }
}
