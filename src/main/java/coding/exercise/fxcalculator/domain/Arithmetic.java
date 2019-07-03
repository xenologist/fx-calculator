package coding.exercise.fxcalculator.domain;

/**
 * Defines a set of arithmetic operations on an numeric type.
 * 
 * @param <N>
 *            underlying numeric type
 */
public interface Arithmetic<N> {
    N multiply(N first, N second);

    N invert(N value);
}
