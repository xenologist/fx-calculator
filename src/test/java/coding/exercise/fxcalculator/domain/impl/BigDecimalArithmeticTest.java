package coding.exercise.fxcalculator.domain.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.domain.impl.BigDecimalArithmetic;

public class BigDecimalArithmeticTest {
    private BigDecimalArithmetic sut;

    @Before
    public void setUp() {
        sut = new BigDecimalArithmetic(4, RoundingMode.HALF_UP);
    }

    @Test
    public void testMultiply() {
        assertEquals(new BigDecimal("1"), sut.multiply(new BigDecimal("1.0"), new BigDecimal("1.000")));
        assertEquals(new BigDecimal("6"), sut.multiply(new BigDecimal("2"), new BigDecimal("3")));
        assertEquals(new BigDecimal("1"), sut.multiply(new BigDecimal("2"), new BigDecimal("0.5")));
        assertEquals(new BigDecimal("11.111"), sut.multiply(new BigDecimal("3.3333333"), new BigDecimal("3.3333")));
    }

    @Test
    public void testInvert() {
        assertEquals(new BigDecimal("1"), sut.invert(new BigDecimal("1")));
        assertEquals(new BigDecimal("1"), sut.invert(new BigDecimal("1.000000")));
        assertEquals(new BigDecimal("0.5"), sut.invert(new BigDecimal("2")));
        assertEquals(new BigDecimal("0.3333"), sut.invert(new BigDecimal("3")));
        assertEquals(new BigDecimal("0.1667"), sut.invert(new BigDecimal("6")));
        assertEquals(new BigDecimal("3"), sut.invert(new BigDecimal("0.33333")));
    }

    @Test(expected = ArithmeticException.class)
    public void testInvert_whenValueIsZero_thenThrowException() {
        sut.invert(new BigDecimal("0.000000"));
    }
}
