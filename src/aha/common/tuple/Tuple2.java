package aha.common.tuple;

import java.util.Objects;

public record Tuple2<A, B>(A first, B second) {

    public static <A, B> Tuple2<A, B> of(A a, B b) {
    	return new Tuple2<>(a, b); }

    @Override public String toString() {
    	return "(" + first + ", " + second + ")"; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2<?, ?> other)) return false;
        return Objects.equals(first, other.first)
            && Objects.equals(second, other.second);
    }

    @Override public int hashCode() { return Objects.hash(first, second); }
}
