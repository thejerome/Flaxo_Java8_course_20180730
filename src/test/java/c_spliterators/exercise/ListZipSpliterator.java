package c_spliterators.exercise;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private final List<L> list1;
    private final List<R> list2;
    private final Iterator<L> list1Iterator;
    private final Iterator<R> list2Iterator;
    private final BiFunction<L, R, T> combiner;
    private int startInclusive;
    private final int endExclusive;


    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this(list1, list2, combiner, 0);
    }

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner,
                               int startInclusive) {
        this.list1 = list1;
        this.list2 = list2;
        this.list1Iterator = list1.iterator();
        this.list2Iterator = list2.iterator();
        this.combiner = combiner;
        this.startInclusive = startInclusive;
        this.endExclusive = list1.size() > list2.size() ? list2.size() : list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if (startInclusive >= endExclusive) {
            return false;
        }
        final L elem1 = list1.get(startInclusive);
        final R elem2 = list2.get(startInclusive);
        final T combined = combiner.apply(elem1, elem2);
        action.accept(combined);
        startInclusive += 1;
        return true;
//        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        final int length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }

        final int mid = startInclusive + length / 2;

        final List<L> resultList1 = list1.subList(startInclusive, mid);
        final List<R> resultList2 = list2.subList(startInclusive, mid);

        startInclusive = mid;

        return new ListZipSpliterator<>(resultList1, resultList2, combiner);
//        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        //TODO
        return endExclusive - startInclusive;
//        throw new UnsupportedOperationException();
    }

    @Override
    public int characteristics() {
        //TODO
        return IMMUTABLE
                | SIZED
                | SUBSIZED
                | NONNULL
                | DISTINCT;
//        throw new UnsupportedOperationException();
    }
}
