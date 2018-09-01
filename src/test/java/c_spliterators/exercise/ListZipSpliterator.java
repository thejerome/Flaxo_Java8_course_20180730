package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

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
        throw new UnsupportedOperationException();
    }
}
