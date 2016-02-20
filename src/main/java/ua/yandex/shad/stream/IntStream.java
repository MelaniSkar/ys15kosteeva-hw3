package ua.yandex.shad.stream;

import ua.yandex.shad.function.IntUnaryOperator;
import ua.yandex.shad.function.IntToIntStreamFunction;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.function.IntConsumer;
import ua.yandex.shad.function.IntBinaryOperator;

public interface IntStream {

    Double average();

    Integer max();

    Integer min();
    
    IntStream flatMap(IntToIntStreamFunction func);

    long count();

    IntStream filter(IntPredicate predicate);

    void forEach(IntConsumer action);

    IntStream map(IntUnaryOperator mapper);

    int reduce(int identity, IntBinaryOperator op);

    Integer sum();

    int[] toArray();
}
