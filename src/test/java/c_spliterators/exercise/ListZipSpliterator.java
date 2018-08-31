package c_spliterators.exercise;

import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;

    private int startInclusive;
    private int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        if (Objects.isNull(list1) || Objects.isNull(list2)) {
            throw new IllegalArgumentException("Arguments shouldn't be NULL");
        }

        if (list1.size() > list2.size()) {
            list1 = list1.subList(0, list2.size());
        } else if (list2.size() > list1.size()) {
            list2 = list2.subList(0, list1.size());
        }

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;

        startInclusive = 0;
        endExclusive = list1.size();
    }

    private ListZipSpliterator(
            List<L> list1, List<R> list2, BiFunction<L, R, T> combiner,
            int startInclusive, int endExclusive
    )
    {

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {

        //TODO
        if (startInclusive < endExclusive) {
            action.accept(combiner.apply(list1.get(startInclusive), list2.get(startInclusive)));
            ++startInclusive;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<T> trySplit() {

        int length = endExclusive - startInclusive;

        //TODO
        if (length <= 1) {
            return null;
        }

        int middle = startInclusive + length / 2;
        int newStartInclusive = startInclusive;

        startInclusive = middle;

        return new ListZipSpliterator<>(list1, list2, combiner, newStartInclusive, middle);
    }

    @Override
    public long estimateSize() {
        //TODO
        return endExclusive - startInclusive;
    }

    @Override
    public int characteristics() {
        //TODO
        return Spliterator.IMMUTABLE
                | Spliterator.ORDERED
                | Spliterator.SIZED
                | Spliterator.SUBSIZED
                | Spliterator.NONNULL;
    }
}
