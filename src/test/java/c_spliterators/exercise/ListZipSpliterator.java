package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;
    private int startIndex;
    private int endIndex;

    ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        this.list1 = (list1.size() > list2.size()) ? list1.subList(0, list2.size()) : list1;
        this.list2 = (list1.size() < list2.size()) ? list2.subList(0, list1.size()) : list2;
        this.combiner = combiner;

        this.startIndex = 0;
        this.endIndex = this.list1.size();
    }

    private ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner,
                                int start, int end){

        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startIndex = start;
        this.endIndex = end;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (startIndex < endIndex) {
            action.accept(combiner.apply(list1.get(startIndex), list2.get(startIndex++)));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        long estimateSize = estimateSize();

        if (estimateSize <= 1) {
            return null;
        }
        int middleIndex = startIndex + (int)estimateSize / 2;
        int newStartIndex = startIndex;
        startIndex = middleIndex;

        return new ListZipSpliterator<>(list1, list2, combiner, newStartIndex, middleIndex);
    }

    @Override
    public long estimateSize() {
        return endIndex - startIndex;
    }

    @Override
    public int characteristics() {
        return Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL |
            Spliterator.ORDERED | Spliterator.SIZED;
    }
}
