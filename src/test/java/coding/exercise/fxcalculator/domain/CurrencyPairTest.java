package coding.exercise.fxcalculator.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.domain.CurrencyPair;

public class CurrencyPairTest {
    private static final String AUD = "AUD";
    private static final String USD = "USD";
    private static final String AUDUSD = "AUDUSD";
    private static final String USDAUD = "USDAUD";

    private CurrencyPair sut;

    @Before
    public void setUp() throws Exception {
        sut = new CurrencyPair(AUD, USD);
    }

    @Test
    public void testGetBaseCurrency() {
        assertEquals(AUD, sut.getBaseCurrency());
    }

    @Test
    public void testGetTermsCurrency() {
        assertEquals(USD, sut.getTermsCurrency());
    }

    @Test
    public void testGetCode() {
        assertEquals(AUDUSD, sut.getCode());
    }

    @Test
    public void testInvert() {
        CurrencyPair inverted = sut.invert();

        assertEquals(USD, inverted.getBaseCurrency());
        assertEquals(AUD, inverted.getTermsCurrency());
        assertEquals(USDAUD, inverted.getCode());

        assertEquals(sut, inverted.invert());
    }

    @Test
    public void testEquals() {
        assertTrue(sut.equals(sut));
        assertTrue(sut.equals(new CurrencyPair(AUD, USD)));
        assertFalse(sut.equals(new CurrencyPair(USD, AUD)));
        assertFalse(sut.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(sut.hashCode(), new CurrencyPair(AUD, USD).hashCode());
    }
}
