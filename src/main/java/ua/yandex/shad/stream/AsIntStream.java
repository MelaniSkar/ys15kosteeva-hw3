package ua.yandex.shad.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ua.yandex.shad.function.IntUnaryOperator;
import ua.yandex.shad.function.IntToIntStreamFunction;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.function.IntConsumer;
import ua.yandex.shad.function.IntBinaryOperator;

public class AsIntStream implements IntStream {

    private MyList<Integer> stream;
    private MyList<Object> functions;

    private AsIntStream() {
        stream = new MyList<>();
        functions = new MyList<>();
    }

    private class ListElement<T> {

        private T value;
        private ListElement next;

        public ListElement(T value) {
            this.value = value;
            next = null;
        }

        public T getValue() {
            return value;
        }

        public ListElement getNext() {
            return next;
        }
    }

    private class MyList<T> implements Iterable<T> {

        private ListElement head;
        private ListElement tail;
        private int length = 0;

        void addLast(T listElement) {

            if (head == null) {
                ListElement current = new ListElement(listElement);
                head = current;
                tail = current;
            } else {
                ListElement current = new ListElement(listElement);
                tail.next = current;
                tail = current;
            }
            length++;
        }

        void printList() {
            ListElement t = head;
            while (t != null) {
                System.out.print(t.value + " ");
                t = t.next;
            }
        }

        boolean contains(T value) {
            if (stream.head == null) {
                throw new IllegalArgumentException();
            }
            ListElement current = stream.head;
            while (current.getNext() != null) {
                if (current.next.value == value) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        void deleteElement(T value) {
            if (head == null) {
                return;
            }
            if (head == tail) {
                head = null;
                tail = null;
                length--;
                return;
            }

            if (head.value == value) {
                head = head.next;
                length--;
                return;
            }

            ListElement t = head;
            while (t.next != null) {
                if (t.next.value == value) {
                    if (tail == t.next) {
                        tail = t;
                    }
                    t.next = t.next.next;
                    length--;
                    return;
                }
                t = t.next;
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new MyListIterator();
        }

        private class MyListIterator<T> implements Iterator<T> {

            private ListElement cur = head;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T res = (T) cur.value;
                cur = cur.next;
                return res;
            }
        }

    }

    @Override
    public void forEach(IntConsumer action) {
        doFunctions();
        for (int current : stream) {
            action.accept(current);
        }
    }

    public static IntStream of(int... values) {

        AsIntStream curStream = new AsIntStream();
        for (int i : values) {
            curStream.stream.addLast(i);
        }
        return curStream;
    }

    @Override
    public Double average() {
        doFunctions();
        if (stream.head == null) {
            throw new IllegalArgumentException();
        }
        int sum = 0;
        ListElement current = stream.head;
        while (current.next != null) {
            sum += (int) current.getValue();
            current = current.next;
        }
        sum += (int) current.getValue();
        double avg = (double) sum / (double) stream.length;
        return avg;
    }

    @Override
    public Integer max() {
        doFunctions();
        if (stream.head == null) {
            throw new IllegalArgumentException();
        }
        int max = (int) stream.head.value;
        ListElement current = stream.head;
        while (current.next != null) {
            if ((int) current.next.value > max) {
                max = (int) current.next.value;
            }
            current = current.next;
        }
        return max;
    }

    @Override
    public Integer min() {
        doFunctions();
        if (stream.head == null) {
            throw new IllegalArgumentException();
        }
        int min = (int) stream.head.value;
        ListElement current = stream.head;
        while (current.next != null) {
            if ((int) current.next.value < min) {
                min = (int) current.next.value;
            }
            current = current.next;
        }
        return min;
    }

    @Override
    public long count() {
        doFunctions();
        MyList<Integer> distinctValues = new MyList();
        long count = 0;
        if (stream.head == null) {
            return 0;
        }
        ListElement current = stream.head;
        distinctValues.addLast((int) current.value);
        count++;
        while (current.next != null) {
            if (distinctValues.contains((int) current.next.value)) {
                distinctValues.addLast((int) current.next.value);
                count++;
            }
            current = current.next;
        }
        return count;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        functions.addLast(predicate);
        return this;
    }

    private void doFunctions() {
        for (Object current : functions) {
            if (current instanceof IntPredicate) {
                doFilter((IntPredicate) current);
            } else if (current instanceof IntUnaryOperator) {
                doMap((IntUnaryOperator) current);
            } else {
                doFlatMap((IntToIntStreamFunction) current);
            }
        }
        functions.head = null;
        functions.tail = null;
        functions.length = 0;
    }

    private void doFilter(IntPredicate predicate) {
        MyList resultList = new MyList();
        stream.printList();
        for (Integer value : stream) {
            System.out.println(resultList.length);
            if (predicate.test(value)) {
                System.out.println(resultList.length);
                resultList.addLast(value);
            }
        }
        stream = resultList;
    }

    private void doMap(IntUnaryOperator unOperator) {
        MyList<Integer> newValues = new MyList<>();
        for (Integer current : stream) {
            int res = unOperator.apply(current);
            newValues.addLast(res);
        }
        stream = newValues;
    }

    private void doFlatMap(IntToIntStreamFunction strFunction) {
        MyList<Integer> newValues = new MyList<>();
        for (Integer current : stream) {
            AsIntStream newIntStream
                    = (AsIntStream) strFunction.applyAsIntStream(current);
            for (Integer cur : newIntStream.stream) {
                newValues.addLast(cur);
            }
        }
        stream = newValues;
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        functions.addLast(mapper);
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        doFunctions();
        int res = identity;
        for (int current : stream) {
            res = op.apply(res, current);
        }
        return res;
    }

    @Override
    public Integer sum() {
        int sum = 0;
        ListElement current = stream.head;
        while (current.getNext() != null) {
            sum += (int) current.getValue();
            current = current.next;
        }
        sum += (int) current.getValue();
        return sum;
    }

    @Override
    public int[] toArray() {
        int[] array = new int[stream.length];
        ListElement current = stream.head;
        for (int i = 0; i < stream.length; i++) {
            array[i] = (int) current.value;
            current = current.next;
        }
        return array;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        functions.addLast(func);
        return this;
    }

}
