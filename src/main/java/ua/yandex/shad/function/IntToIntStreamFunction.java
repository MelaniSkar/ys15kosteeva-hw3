package ua.yandex.shad.function;

import ua.yandex.shad.stream.IntStream;

public interface IntToIntStreamFunction {
     IntStream applyAsIntStream(int value);
}
