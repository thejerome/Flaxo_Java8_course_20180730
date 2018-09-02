package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;
    private int leftBound;
    private final int rightBound;
    private int index;

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner, int leftBound, int rightBound, int index) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.index = index;
    }

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this(list1, list2, combiner, 0, Math.min(list1.size(), list2.size()), 0);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if (index < rightBound) {
            action.accept(combiner.apply(list1.get(index), list2.get(index)));
            ++index;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        final int width = rightBound - leftBound;
        if (width < 2)
            return null;
        final int newSpliteratorRightBound = leftBound + width / 2;
        Spliterator<T> newSpliterator = new ListZipSpliterator<>(list1, list2, combiner, 0, newSpliteratorRightBound, index);
        this.leftBound = newSpliteratorRightBound;
        this.index = leftBound;
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return rightBound - leftBound;
    }

    @Override
    public int characteristics() {
        //TODO
        return ORDERED | SIZED | SUBSIZED;
    }
}
