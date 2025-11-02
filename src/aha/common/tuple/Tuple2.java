package aha.common.tuple;

import java.util.Objects;

/**
 * <p>
 *   Tuple of two elements.
 * </p>
 * @param <A> Type of first element.
 * @param <B> Type of second element.
 */
public record Tuple2<A, B>(A first, B second) {

	/**
	 * <p>
	 *   Creates tuple of two elements.
	 * </p>
	 * @param <A> Type of first element.
	 * @param <B> Type of second element.
	 * @param a First element.
	 * @param b Second element.
	 * @return Tuple of two elements where first is {@code a} and second is 
	 *         {@code b}.
	 */
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
