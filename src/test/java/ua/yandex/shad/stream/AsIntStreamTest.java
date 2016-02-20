package ua.yandex.shad.stream;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class AsIntStreamTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAverageWithEmptyArray() {
        AsIntStream.of().average();
    }

    @Test
    public void testAverageArrayWithElements() {
        IntStream test = AsIntStream.of(1, 2, 3, 5);
        double res = test.average();
        double expectRes = 2.75;
        assertEquals(res, expectRes, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxEmptyArray() {
        AsIntStream.of().max();
    }

    @Test
    public void testMaxArrayWithElements() {
        IntStream test = AsIntStream.of(142, 2, 52, 91);
        int res = test.max();
        int expectRes = 142;
        assertEquals(res, expectRes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinEmptyArray() {
        AsIntStream.of().min();
    }

    @Test
    public void testMinArrayWithElements() {
        IntStream test = AsIntStream.of(142, 2, 52, 91);
        int res = test.min();
        int expectRes = 2;
        assertEquals(res, expectRes);
    }

    @Test
    public void testCountEmptyArray() {
        IntStream test = AsIntStream.of();
        long res = test.count();
        long expectRes = 0;
        assertEquals(res, expectRes);
    }

    @Test
    public void testCountArrayWithSeveralElements() {
        IntStream test = AsIntStream.of(142, 2, 52, 91);
        long res = test.count();
        long expectRes = 4;
        assertEquals(res, expectRes);
    }

    @Test
    public void testFilterArrayWithElementsEmptyOut() {
        IntStream test = AsIntStream.of(5, 6, 8, 5);
        test.filter(x -> (x < 0));
        int[] res = test.toArray();
        int[] expectRes = {5, 6, 8, 5};
        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testFilterArrayWithElementsGodResult() {
        IntStream test = AsIntStream.of(5, 1, 2, 5, 9, 1);
        test.filter(x -> (x - 1) == 0);
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {1, 1};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testFilterArrayWithElementsAllElements() {
        IntStream test = AsIntStream.of(5, 1, 2, 6, 9, 1);
        test.filter(x -> x >= 4);
        int[] res = test.toArray();
        int[] expectRes = {5, 6, 9};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testMapEmptyArray() {
        IntStream test = AsIntStream.of();
        test.map(x -> x - 1);
        int[] res = test.toArray();
        int[] expectRes = {};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testMapArrayWithElements() {
        IntStream test = AsIntStream.of(5, 6, 8, 5);
        test.map(x -> x + 1);
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {6, 7, 9, 6};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testMapArrayWithElementsMoreElements() {
        IntStream test = AsIntStream.of(0, 1, 2, 5, 11, 1);
        test.map(x -> x * 5);
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {0, 5, 10, 25, 55, 5};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testReduceEmptyArray() {
        IntStream test = AsIntStream.of();
        int res = test.reduce(0, (sum, x) -> sum += x);
        int expectRes = 0;

        assertEquals(res, expectRes);
    }

    @Test
    public void testReduceArrayWithElements() {
        IntStream test = AsIntStream.of(5, 6, 8, 5);
        int res = test.reduce(0, (sum, x) -> sum += x);
        int expectRes = 24;

        assertEquals(res, expectRes);
    }

    @Test
    public void testReduceArrayWithElementsMoreElements() {
        IntStream test = AsIntStream.of(5, 1, 2, 5, 9, 1);
        int res = test.reduce(0, (sum, x) -> sum += x);
        int expectRes = 23;

        assertEquals(res, expectRes);
    }

    @Test(expected = NullPointerException.class)
    public void testSumEmptyArray() {
        AsIntStream.of().sum();
    }

    @Test
    public void testSumArrayWithElements() {
        IntStream test = AsIntStream.of(142, 2, 52, 91);
        int res = test.sum();
        int expectRes = 287;
        assertEquals(res, expectRes);
    }

    @Test
    public void testSumArrayWithElementsZeroResult() {
        IntStream test = AsIntStream.of(142, -7, 2, 52, 91, -280);
        int res = test.sum();
        int expectRes = 0;
        assertEquals(res, expectRes);
    }

    @Test
    public void testToArrayEmptyArray() {
        IntStream test = AsIntStream.of();
        int[] res = test.toArray();
        int[] expectRes = {};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testToArrayArrayWithElements() {
        IntStream test = AsIntStream.of(5, 6, 8, 5);
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {5, 6, 8, 5};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatMapEmptyArray() {
        IntStream test = AsIntStream.of();
        test.flatMap(x -> AsIntStream.of(x - 1, x, x + 1));
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {};

        Assert.assertArrayEquals(res, expectRes);
    }

    @Test
    public void testFlatMapArrayWithElements() {
        IntStream test = AsIntStream.of(5, 8, 10);
        test.flatMap(x -> AsIntStream.of(x - 1, x, x + 1));
        test.min();
        int[] res = test.toArray();
        int[] expectRes = {4, 5, 6, 7, 8, 9, 9, 10, 11};

        Assert.assertArrayEquals(res, expectRes);
    }
}
