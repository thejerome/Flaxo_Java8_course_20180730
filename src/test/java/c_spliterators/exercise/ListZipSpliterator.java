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
        if (current == list1.size() || current == list2.size()) {
            return false;
        }
        L first = list1.get(current);
        R second = list2.get(current);
        T combined = combiner.apply(first, second);
        action.accept(combined);
        current++;
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        if (list1.size() < 2 && list2.size() < 2) {
            return null;
        }
        ListZipSpliterator<L, R, T> zipSpliterator;
        if (list1.size() > list2.size()) {
            int mid = list1.size() / 2;
            zipSpliterator = new ListZipSpliterator<>(list1.subList(0, mid), list2.subList(0, mid), combiner);
            current = mid;
        } else {
            int mid = list2.size() / 2;
            zipSpliterator = new ListZipSpliterator<>(list1.subList(0, mid), list2.subList(0, mid), combiner);
            current = mid;
        }
        return zipSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return Math.min (list1.size(), list2.size());
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
