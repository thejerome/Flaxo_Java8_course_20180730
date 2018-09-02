package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {


    private final List<L> firstList;
    private final List<R> secondList;
    private final BiFunction<L, R, T> combineFunction;

    private int start;
    private int end;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {

        if (list1.size() < list2.size()) {
            list2 = list2.subList(0, list1.size());
        }
        if (list1.size() > list2.size()) {
            list1 = list1.subList(0, list2.size());
        }

        this.firstList = list1;
        this.secondList = list2;
        this.combineFunction = combiner;

        start = 0;
        end = list1.size();
    }

    private ListZipSpliterator(List<L> firstList, List<R> secondList,
        BiFunction<L, R, T> combineFunction, int newStart, int middle) {

        this.start = newStart;
        this.end = middle;
        this.secondList = secondList;
        this.firstList = firstList;
        this.combineFunction = combineFunction;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {

        if (start < end) {
            action.accept(combineFunction.apply(firstList.get(start), secondList.get(start++)));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {

        int length = end - start;
        if (length <= 1) {
            return null;
        }

        int middle = start + length / 2;
        int newStart = start;

        start = middle;

        return new ListZipSpliterator<>(firstList, secondList, combineFunction, newStart, middle);
    }

    @Override
    public long estimateSize() {
        return end - start;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | ORDERED | NONNULL | SIZED | SUBSIZED;
    }
}
