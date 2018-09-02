package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;
    private int current;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this(list1, list2, combiner, 0);
    }

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int current) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.current = current;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public int characteristics() {
        //TODO
        return IMMUTABLE
            | NONNULL
            | ORDERED
            | SIZED
            | SUBSIZED;
    }
}
