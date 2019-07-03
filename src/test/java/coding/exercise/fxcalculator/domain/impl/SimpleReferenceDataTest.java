package coding.exercise.fxcalculator.domain.impl;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import coding.exercise.fxcalculator.domain.CurrencyPair;
import coding.exercise.fxcalculator.domain.impl.SimpleReferenceData;

public class SimpleReferenceDataTest {
    private static final String AUD = "AUD";
    private static final String JPY = "JPY";
    private static final String USD = "USD";
    private static final String OMG = "OMG";
    private static final String WTF = "WTF";

    private static final CurrencyPair AUDUSD = new CurrencyPair(AUD, USD);
    private static final CurrencyPair USDAUD = new CurrencyPair(USD, AUD);
    private static final CurrencyPair USDJPY = new CurrencyPair(USD, JPY);
    private static final CurrencyPair JPYUSD = new CurrencyPair(JPY, USD);
    private static final CurrencyPair AUDJPY = new CurrencyPair(AUD, JPY);
    private static final CurrencyPair JPYAUD = new CurrencyPair(JPY, AUD);
    private static final CurrencyPair OMGWTF = new CurrencyPair(OMG, WTF);

    private SimpleReferenceData sut;

    @Before
    public void setUp() {
        sut = new SimpleReferenceData(1);

        sut.addFeedCurrencyPair(AUDUSD);
        sut.addFeedCurrencyPair(USDJPY);

        sut.addSplitCurrency(AUDJPY, USD);

        sut.addDisplayPrecision(AUD, 2);
        sut.addDisplayPrecision(JPY, 0);
        sut.addDisplayPrecision(USD, 2);
    }

    @Test
    public void testGetFeedCurrencyPair() {
        assertEquals(Optional.of(AUDUSD), sut.getFeedCurrencyPair(AUDUSD));
        assertEquals(Optional.of(AUDUSD), sut.getFeedCurrencyPair(USDAUD));
        assertEquals(Optional.of(USDJPY), sut.getFeedCurrencyPair(USDJPY));
        assertEquals(Optional.of(USDJPY), sut.getFeedCurrencyPair(JPYUSD));
        assertEquals(Optional.empty(), sut.getFeedCurrencyPair(AUDJPY));
        assertEquals(Optional.empty(), sut.getFeedCurrencyPair(JPYAUD));
        assertEquals(Optional.empty(), sut.getFeedCurrencyPair(OMGWTF));
    }

    @Test
    public void testGetSplitCurrency() {
        assertEquals(Optional.empty(), sut.getSplitCurrency(AUDUSD));
        assertEquals(Optional.empty(), sut.getSplitCurrency(USDAUD));
        assertEquals(Optional.empty(), sut.getSplitCurrency(USDJPY));
        assertEquals(Optional.empty(), sut.getSplitCurrency(JPYUSD));
        assertEquals(Optional.of(USD), sut.getSplitCurrency(AUDJPY));
        assertEquals(Optional.of(USD), sut.getSplitCurrency(JPYAUD));
        assertEquals(Optional.empty(), sut.getSplitCurrency(OMGWTF));
    }

    @Test
    public void testGetDisplayPrecision() {
        assertEquals(2, sut.getDisplayPrecision(AUD));
        assertEquals(0, sut.getDisplayPrecision(JPY));
        assertEquals(2, sut.getDisplayPrecision(USD));
        assertEquals(1, sut.getDisplayPrecision(OMG)); // default
        assertEquals(1, sut.getDisplayPrecision(WTF)); // default
    }
}
