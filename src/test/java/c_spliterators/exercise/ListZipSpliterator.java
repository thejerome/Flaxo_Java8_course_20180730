package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {
    private final List<L> listL;
    private final List<R> listR;
    private final BiFunction<L, R, T> combiner;
    private final int characteristics;
    private int start;
    private int end;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        if (list1.size() > list2.size()) {
            this.listL = list1.subList(0, list2.size());
            this.listR = list2;
        } else {
            this.listL = list1;
            this.listR = list2.subList(0, list1.size());
        }
        this.combiner = combiner;
        this.start = 0;
        this.end = this.listL.size();
        this.characteristics = IMMUTABLE
                | NONNULL
                | DISTINCT
                | ORDERED
                | SIZED
                | SUBSIZED
        ;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (start >= end) {
            return false;
        } else {
            action.accept(combiner.apply(listL.get(start), listR.get(start++)));
            return true;
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        final int length = end - start;
        if (length < 2) {
            return null;
        }
        final int middle = start + length / 2;
        final ListZipSpliterator<L, R, T> listZipSpliterator = new ListZipSpliterator<>(
                listL.subList(0, middle),
                listR.subList(0, middle),
                combiner);
        start = middle;
        return listZipSpliterator;
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }
}
