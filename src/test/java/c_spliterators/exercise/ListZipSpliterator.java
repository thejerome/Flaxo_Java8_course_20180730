package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private BiFunction<L, R, T> combiner;
    private int startInclusive;
    private final int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        if (list1.size() > list2.size()) {
            this.list1 = list1.subList(0, list2.size());
            this.list2 = list2;
        } else {
            this.list1 = list1;
            this.list2 = list2.subList(0, list1.size());
        }

        this.combiner = combiner;
        this.startInclusive = 0;
        this.endExclusive = this.list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {

        if (startInclusive >= endExclusive) {
            return false;
        }

        action.accept(combiner.apply(list1.get(startInclusive), list2.get(startInclusive++)));

        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        final int length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }

        final int mid = startInclusive + length / 2;

        final ListZipSpliterator<L, R, T> res = new ListZipSpliterator<>(
                list1.subList(0, mid),
                list2.subList(0, mid),
                combiner);

        startInclusive = mid;
        return res;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE
                | ORDERED
                | SIZED
                | SUBSIZED
                | NONNULL;
    }
}
