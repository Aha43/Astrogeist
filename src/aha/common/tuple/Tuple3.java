package aha.common.tuple;

import java.util.Objects;

/**
 * <p>
 *   Tuple of three elements.
 * </p>
 * @param <A> Type of first element.
 * @param <B> Type of second element.
 * @param <C> Type of third element.
 */
public record Tuple3<A, B, C>(A first, B second, C third) {

	/**
	 * <p>
	 *   Creates tuple of three elements.
	 * </p>
	 * @param <A> Type of first element.
	 * @param <B> Type of second element.
	 * @param <C> Type of third element. 
	 * @param a First element.
	 * @param b Second element.
	 * @param c Third element.
	 * @return Tuple of three elements where first is {@code a}, second is 
	 *         {@code b} and third is {@code c}.
	 */
    public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) { 
    	return new Tuple3<>(a, b, c); }

    @Override public String toString() {
    	return "(" + first + ", " + second + ", " + third + ")"; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple3<?, ?, ?> other)) return false;
        return Objects.equals(first, other.first)
            && Objects.equals(second, other.second)
        	&& Objects.equals(third, other.third);
    }

    @Override public int hashCode() {
    	return Objects.hash(first, second, third); }
}

