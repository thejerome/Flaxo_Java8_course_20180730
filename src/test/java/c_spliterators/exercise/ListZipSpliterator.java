package c_spliterators.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T> implements Spliterator<T> {

    private int start;
    private int end;
    private final List<L> list1;
    private final List<R> list2;
    private final BiFunction<L, R, T> combiner;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.combiner = combiner;

        if(list1.size() > list2.size()){
            this.list1 = list1.subList(0, list2.size());
            this.list2 = list2;
        } else {
            this.list2 = list2.subList(0, list1.size());
            this.list1 = list1;
        }

        this.start = 0;
        this.end = this.list1.size();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //TODO
        if (start < end){
            action.accept(combiner.apply(list1.get(start), list2.get(start++)));
            return true;
        }

        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        //TODO
        if (estimateSize() < 1) return null;

        int mid = (int) (start + estimateSize() / 2);

        ListZipSpliterator<L, R, T> spliterator = new ListZipSpliterator<>(list1.subList(0, mid),
            list2.subList(0, mid),
            combiner);

        start = mid;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        //TODO
        return end - start;
    }

    @Override
    public int characteristics() {
        //TODO
        return IMMUTABLE
            |NONNULL
            |DISTINCT
            |ORDERED
            |SIZED
            |SUBSIZED;
    }
}
