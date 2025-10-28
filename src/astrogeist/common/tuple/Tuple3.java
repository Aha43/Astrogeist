package astrogeist.common.tuple;

import java.util.Objects;

public record Tuple3<A, B, C>(A first, B second, C third) {

    public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) { return new Tuple3<>(a, b, c); }

    @Override public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple3<?, ?, ?> other)) return false;
        return Objects.equals(first, other.first)
            && Objects.equals(second, other.second)
        	&& Objects.equals(third, other.third);
    }

    @Override public int hashCode() { return Objects.hash(first, second, third); }
}

